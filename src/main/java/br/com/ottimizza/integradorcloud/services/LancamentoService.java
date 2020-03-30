package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ottimizza.integradorcloud.client.DeParaClient;
import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.commands.lancamento.ImportacaoLancamentosRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.arquivo.ArquivoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.depara.DeParaContaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.organization.OrganizationDTO;
import br.com.ottimizza.integradorcloud.domain.exceptions.lancamento.LancamentoNaoEncontradoException;
import br.com.ottimizza.integradorcloud.domain.mappers.ArquivoMapper;
import br.com.ottimizza.integradorcloud.domain.mappers.lancamento.LancamentoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Arquivo;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.domain.models.KPILancamento;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.repositories.arquivo.ArquivoRepository;
import br.com.ottimizza.integradorcloud.repositories.empresa.EmpresaRepository;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.lancamento.LancamentoRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;

@Service // @formatter:off
public class LancamentoService {

	@Inject
	LancamentoRepository lancamentoRepository;

	@Inject
	GrupoRegraRepository grupoRegraRepository;

	@Inject
	RegraRepository regraRepository;

	@Inject
	ArquivoRepository arquivoRepository;

	@Inject
	EmpresaRepository empresaRepository;

	@Inject
	DeParaClient deParaContaClient;

	@Inject
	OAuthClient oauthClient;

	public Lancamento buscarPorId(BigInteger id) throws LancamentoNaoEncontradoException {
		return lancamentoRepository.findById(id).orElseThrow(() -> new LancamentoNaoEncontradoException(
				"Não foi encontrado nenhum lançamento com o Id especificado!"));
	}

	public Page<LancamentoDTO> buscarTodos(LancamentoDTO filter, PageCriteria criteria, Principal principal)
			throws Exception {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		Example<Lancamento> example = Example.of(LancamentoMapper.fromDto(filter), matcher);
		return lancamentoRepository.findAll(example, LancamentoDTO.getPageRequest(criteria))
				.map(LancamentoMapper::fromEntity);
	}

	public KPILancamento buscaStatusLancementosPorCNPJEmpresa(String cnpjEmpresa, OAuth2Authentication authentication)
			throws Exception {
		return lancamentoRepository.buscaStatusLancementosPorCNPJEmpresa(cnpjEmpresa);
	}

	public String apagarTodos(LancamentoDTO filter, PageCriteria criteria, boolean limparRegras, Principal principal)
			throws Exception {
		Integer affectedRows = lancamentoRepository.apagarTodosPorCnpjEmpresa(filter.getCnpjEmpresa());
		if (limparRegras) {
			regraRepository.apagarTodosPorCnpjEmpresa(filter.getCnpjEmpresa());
			grupoRegraRepository.apagarTodosPorCnpjEmpresa(filter.getCnpjEmpresa());
		}
		if (affectedRows > 0) {
			return MessageFormat.format("{0} lançamentos excluídos. ", affectedRows);
		}
		return "Nenhum registro excluído!";
	}

	public LancamentoDTO buscarPorId(BigInteger id, Principal principal) throws LancamentoNaoEncontradoException {
		return LancamentoMapper.fromEntity(buscarPorId(id));
	}

	//
	//
	public LancamentoDTO salvar(LancamentoDTO lancamentoDTO, OAuth2Authentication authentication) throws Exception {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
		String accessToken = details.getTokenValue();
		String authorization = MessageFormat.format("Bearer {0}", accessToken);

		Lancamento lancamento = LancamentoMapper.fromDto(lancamentoDTO);

		// Busca detalhes da empresa relacionada aos lançamento importados.
		GenericPageableResponse<OrganizationDTO> response = oauthClient
				.buscarEmpresasPorCNPJ(lancamentoDTO.getCnpjEmpresa(), authorization).getBody();
		if (response.getPageInfo().getTotalElements() == 1) {
			OrganizationDTO organizationDTO = response.getRecords().get(0);
			Empresa empresa = Empresa.builder().razaoSocial(organizationDTO.getName())
					.cnpj(organizationDTO.getCnpj().replaceAll("\\D*", "")).codigoERP(organizationDTO.getCodigoERP())
					.organizationId(organizationDTO.getId()).accountingId(organizationDTO.getOrganizationId()).build();
			Empresa existente = empresaRepository.buscarPorCNPJ(empresa.getCnpj()).orElse(null);
			if (existente != null && existente.getId() != null) {
				empresa.setId(existente.getId());
			}
			empresaRepository.save(empresa);
		} else if (response.getPageInfo().getTotalElements() == 0) {
			throw new IllegalArgumentException("O cnpj informado não stá cadastrado!");
		} else if (response.getPageInfo().getTotalElements() > 1) {
			throw new IllegalArgumentException("O cnpj informado retornou mais de uma empresa!");
		}

		validaLancamento(lancamento);

		lancamento.setArquivo(arquivoRepository
				.save(lancamento.getArquivo().toBuilder().cnpjContabilidade(lancamentoDTO.getCnpjContabilidade())
						.cnpjEmpresa(lancamentoDTO.getCnpjEmpresa()).build()));

		lancamento.setNomeArquivo(lancamento.getArquivo().getNome());
		lancamento.setCnpjContabilidade(lancamento.getCnpjContabilidade().replaceAll("\\D*", ""));
		lancamento.setCnpjEmpresa(lancamento.getCnpjEmpresa().replaceAll("\\D*", ""));

		return LancamentoMapper.fromEntity(lancamentoRepository.save(lancamento));
	}

	public LancamentoDTO salvar(LancamentoDTO lancamentoDTO, Principal principal) throws Exception {
		Lancamento lancamento = LancamentoMapper.fromDto(lancamentoDTO);

		validaLancamento(lancamento);

		lancamento.setArquivo(arquivoRepository
				.save(lancamento.getArquivo().toBuilder().cnpjContabilidade(lancamentoDTO.getCnpjContabilidade())
						.cnpjEmpresa(lancamentoDTO.getCnpjEmpresa()).build()));

		lancamento.setCnpjContabilidade(lancamento.getCnpjContabilidade().replaceAll("\\D*", ""));
		lancamento.setCnpjEmpresa(lancamento.getCnpjEmpresa().replaceAll("\\D*", ""));

		return LancamentoMapper.fromEntity(lancamentoRepository.save(lancamento));
	}

	public LancamentoDTO salvar(BigInteger id, LancamentoDTO lancamentoDTO, Principal principal) throws Exception {
		Lancamento lancamento = lancamentoDTO.patch(buscarPorId(id));
		validaLancamento(lancamento);
		return LancamentoMapper.fromEntity(lancamentoRepository.save(lancamento));
	}

	//
	//
	public LancamentoDTO salvarTransacaoComoDePara(BigInteger id, String contaMovimento, boolean salvarParaTodos,
			OAuth2Authentication authentication) throws Exception {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
		String accessToken = details.getTokenValue();

		Lancamento lancamento = lancamentoRepository.save(this.buscarPorId(id).toBuilder()
				.contaMovimento(contaMovimento).tipoConta(Lancamento.TipoConta.DEPARA).build());

		String cnpjEmpresa = lancamento.getCnpjEmpresa();

		DeParaContaDTO deParaContaDTO = DeParaContaDTO.builder().cnpjContabilidade(lancamento.getCnpjContabilidade())
				.cnpjEmpresa(lancamento.getCnpjEmpresa()).descricao(lancamento.getDescricao())
				.contaDebito(lancamento.getTipoLancamento().equals(Lancamento.Tipo.PAGAMENTO) ? contaMovimento : null)
				.contaCredito(
						lancamento.getTipoLancamento().equals(Lancamento.Tipo.RECEBIMENTO) ? contaMovimento : null)
				.build();

		deParaContaClient.salvar(deParaContaDTO, "Bearer " + accessToken);

		if (salvarParaTodos) {
			lancamentoRepository.atualizarContaMovimentoPorDescricaoETipoLancamento(lancamento.getDescricao(),
					lancamento.getTipoLancamento(), lancamento.getContaMovimento(), cnpjEmpresa);
		} else {
			lancamentoRepository.atualizarContaSugeridaPorDescricaoETipoLancamento(lancamento.getDescricao(),
					lancamento.getTipoLancamento(), lancamento.getContaMovimento(), cnpjEmpresa);
		}

		return LancamentoMapper.fromEntity(lancamento);
	}

	public LancamentoDTO salvarTransacaoComoOutrasContas(BigInteger id, String contaMovimento, Principal principal)
			throws Exception {
		return LancamentoMapper.fromEntity(lancamentoRepository.save(
				buscarPorId(id).toBuilder().contaMovimento(contaMovimento).tipoConta(Short.parseShort("2")).build()));
	}

	public LancamentoDTO salvarTransacaoComoIgnorar(BigInteger id, OAuth2Authentication authentication)
			throws Exception {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
		String accessToken = details.getTokenValue();

		Lancamento lancamento = lancamentoRepository.save(this.buscarPorId(id).toBuilder().contaMovimento("IGNORAR")
				.tipoConta(Lancamento.TipoConta.IGNORAR).build());

		String cnpjEmpresa = lancamento.getCnpjEmpresa();

		DeParaContaDTO deParaContaDTO = DeParaContaDTO.builder().cnpjContabilidade(lancamento.getCnpjContabilidade())
				.cnpjEmpresa(lancamento.getCnpjEmpresa()).descricao(lancamento.getDescricao())
				.contaDebito(lancamento.getTipoLancamento().equals(Lancamento.Tipo.PAGAMENTO) ? "IGNORAR" : null)
				.contaCredito(lancamento.getTipoLancamento().equals(Lancamento.Tipo.RECEBIMENTO) ? "IGNORAR" : null)
				.build();

		deParaContaClient.salvar(deParaContaDTO, "Bearer " + accessToken);
		lancamentoRepository.atualizarContaSugeridaPorDescricaoETipoLancamento(lancamento.getDescricao(),
				lancamento.getTipoLancamento(), lancamento.getContaMovimento(), cnpjEmpresa);

		return LancamentoMapper.fromEntity(lancamento);
	}

	//
	//
	//
	//
	//
	//
	public Page<LancamentoDTO> buscarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa,
			PageCriteria pageCriteria, Principal principal) throws Exception {
		return lancamentoRepository
				.buscarLancamentosPorRegra(regras, cnpjEmpresa,
						PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize()), principal)
				.map(LancamentoMapper::fromEntity);
	}

	//
	//
	@Transactional(rollbackFor = Exception.class)
	public List<LancamentoDTO> importar(ImportacaoLancamentosRequest importaLancamentos,
			OAuth2Authentication authentication) throws Exception {
		final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
		String accessToken = details.getTokenValue();
		String authorization = MessageFormat.format("Bearer {0}", accessToken);

		List<Lancamento> results = new ArrayList<>();

		// Busca detalhes da empresa relacionada aos lançamento importados.
		OrganizationDTO contabilidade = oauthClient
				.buscaContabilidade(importaLancamentos.getCnpjContabilidade(), 1, true, authorization).getBody()
				.getRecords().get(0);

		GenericPageableResponse<OrganizationDTO> response = oauthClient
				.buscaEmpresa(importaLancamentos.getCnpjEmpresa(), contabilidade.getId(), 2, authorization).getBody();

		if (response.getPageInfo().getTotalElements() == 1) {
			OrganizationDTO organizationDTO = response.getRecords().get(0);
			Empresa empresa = Empresa.builder()
					.razaoSocial(organizationDTO.getName())
					.cnpj(organizationDTO.getCnpj().replaceAll("\\D*", ""))
					.codigoERP(organizationDTO.getCodigoERP())
					.organizationId(organizationDTO.getId())
					.accountingId(organizationDTO.getOrganizationId())
				.build();

			// Usado para encontrar uma empresa quando existe varias com o mesmo cnpj
			Empresa existente = empresaRepository.buscaEmpresa(empresa.getCnpj(), contabilidade.getId()).orElse(null);
			if (existente != null && existente.getId() != null) {
				empresa.setId(existente.getId());
			}
			empresaRepository.save(empresa);
			} else if (response.getPageInfo().getTotalElements() == 0) {
			
			try {
				OrganizationDTO empresaOauth = OrganizationDTO.builder()
								.name(importaLancamentos.getNomeEmpresa())
								.cnpj(importaLancamentos.getCnpjEmpresa().replaceAll("\\D*", ""))
								.codigoERP(importaLancamentos.getCodEmpresa())
								.organizationId(contabilidade.getId())
								.type(2)
						.build();
				Empresa empresaIntegrador = Empresa.builder()
								.razaoSocial(importaLancamentos.getNomeEmpresa())
								.cnpj(empresaOauth.getCnpj().replaceAll("\\D*", ""))
								.codigoERP(empresaOauth.getCodigoERP())
								.organizationId(empresaOauth.getId())
								.accountingId(empresaOauth.getOrganizationId())
						.build();
				
				Empresa existente = empresaRepository.buscaEmpresa(empresaIntegrador.getCnpj(), contabilidade.getId())
						.orElse(null);
				if (existente != null && existente.getId() != null) {
					empresaIntegrador.setId(existente.getId());
				}
				
				empresaRepository.save(empresaIntegrador);

				oauthClient.salvaEmpresa(empresaOauth, authorization);
				
				

			} catch (Exception ex) {
				System.out.println("Entrou no catch de salvar empresas");
				System.out.println("Mensagem "+ex.getMessage());
			}
		} else if (response.getPageInfo().getTotalElements() > 1) {
			throw new IllegalArgumentException("O cnpj informado retornou mais de uma empresa!");
		}

		
		// Cria o Arquivo
		Arquivo arquivo = importaLancamentos.getArquivo();

		// Iteração e construção de lista de lançamentos
		List<Lancamento> lancamentos = importaLancamentos.getLancamentos().stream().map((o) -> {
			return LancamentoMapper.fromDto(o).toBuilder().nomeArquivo(arquivo.getNome()).arquivo(arquivo)
					.cnpjContabilidade(importaLancamentos.getCnpjContabilidade())
					.cnpjEmpresa(importaLancamentos.getCnpjEmpresa()).idRoteiro(importaLancamentos.getIdRoteiro())
					.build();
		}).collect(Collectors.toList());

		// Iteração e validação de lista de lançamentos
		for (Lancamento lancamento : lancamentos) {
			validaLancamento(lancamento);
		}
		lancamentoRepository.saveAll(lancamentos).forEach(results::add);
		return LancamentoMapper.fromEntities(results);
	}

	public ArquivoDTO salvaArquivo(ArquivoDTO filter) {
		Arquivo arquivo = arquivoRepository.findArquivo(filter.getCnpjEmpresa(), filter.getCnpjContabilidade(),
				filter.getNome());

		if (arquivo == null) {
			arquivo = arquivoRepository
					.save(Arquivo.builder().nome(filter.getNome()).cnpjContabilidade(filter.getCnpjContabilidade())
							.cnpjEmpresa(filter.getCnpjEmpresa()).labelComplemento01(filter.getLabelComplemento01())
							.labelComplemento02(filter.getLabelComplemento02())
							.labelComplemento03(filter.getLabelComplemento03())
							.labelComplemento04(filter.getLabelComplemento04())
							.labelComplemento05(filter.getLabelComplemento05())

							.build());

		}

		lancamentoRepository.atualizaStatus(arquivo.getId());

		return ArquivoMapper.fromEntity(arquivo);
	}

	private boolean validaLancamento(Lancamento lancamento) throws Exception {
		if (lancamento.getTipoLancamento() == null) {
			throw new IllegalArgumentException("Informe o tipo do lançamento!");
		}
		if (!Arrays.asList(Lancamento.Tipo.PAGAMENTO, Lancamento.Tipo.RECEBIMENTO)
				.contains(lancamento.getTipoLancamento())) {
			throw new IllegalArgumentException("Informe um tipo de lançamento válido!");
		}
		if (lancamento.getCnpjContabilidade() == null || lancamento.getCnpjContabilidade().equals("")) {
			throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionada ao lançamento!");
		}
		if (lancamento.getCnpjEmpresa() == null || lancamento.getCnpjEmpresa().equals("")) {
			throw new IllegalArgumentException("Informe o cnpj da empresa relacionada ao lançamento!");
		}
		if (lancamento.getIdRoteiro() == null || lancamento.getIdRoteiro().equals("")) {
			throw new IllegalArgumentException("Informe o Id do Roteiro relacionado ao lançamento!");
		}
		if (lancamento.getTipoMovimento() == null || lancamento.getTipoMovimento().equals("")) {
			throw new IllegalArgumentException("Informe o tipo de movimento do lançamento!");
		}
		if (lancamento.getTipoPlanilha() == null || lancamento.getTipoPlanilha().equals("")) {
			throw new IllegalArgumentException("Informe o tipo da planilha!");
		}
		if (lancamento.getArquivo() == null || lancamento.getArquivo().getNome() == null) {
			throw new IllegalArgumentException("Informe o nome do arquivo relacionado ao lançamento!");
		}
		if (lancamento.getDataMovimento() == null) {
			throw new IllegalArgumentException("Informe a data do lançamento!");
		}
		if (lancamento.getDataMovimento().isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("A data do lançamento não pode ser maior que hoje!");
		}
		if (lancamento.getValorOriginal() == null || lancamento.getValorOriginal() <= 0) {
			throw new IllegalArgumentException("Informe o valor do lançamento!");
		}
		return true;
	}

}