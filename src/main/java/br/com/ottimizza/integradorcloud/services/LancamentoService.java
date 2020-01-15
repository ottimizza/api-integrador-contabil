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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ottimizza.integradorcloud.client.DeParaClient;
import br.com.ottimizza.integradorcloud.domain.commands.lancamento.ImportacaoLancamentosRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.depara.DeParaContaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.exceptions.lancamento.LancamentoNaoEncontradoException;
import br.com.ottimizza.integradorcloud.domain.mappers.lancamento.LancamentoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Arquivo;
import br.com.ottimizza.integradorcloud.domain.models.KPILancamento;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.repositories.arquivo.ArquivoRepository;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.lancamento.LancamentoRepository;

@Service // @formatter:off
public class LancamentoService {

    @Inject
    LancamentoRepository lancamentoRepository;
    
    @Inject
    GrupoRegraRepository grupoRegraRepository;

    @Inject
    ArquivoRepository arquivoRepository;

    @Inject
    DeParaClient deParaContaClient;

    public Lancamento buscarPorId(BigInteger id) throws LancamentoNaoEncontradoException {
        return lancamentoRepository.findById(id)
            .orElseThrow(() -> new LancamentoNaoEncontradoException("Não foi encontrado nenhum lançamento com o Id especificado!"));
    }

    public Page<LancamentoDTO> buscarTodos(LancamentoDTO filter, PageCriteria criteria, Principal principal) throws Exception {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING); 
        Example<Lancamento> example = Example.of(LancamentoMapper.fromDto(filter), matcher); 
        return lancamentoRepository.findAll(example, LancamentoDTO.getPageRequest(criteria)).map(LancamentoMapper::fromEntity);
    }

    public KPILancamento buscaStatusLancementosPorCNPJEmpresa(String cnpjEmpresa, OAuth2Authentication authentication) throws Exception {
        return lancamentoRepository.buscaStatusLancementosPorCNPJEmpresa(cnpjEmpresa);
    }
    
    public String apagarTodos(LancamentoDTO filter, PageCriteria criteria, boolean limparRegras, Principal principal) throws Exception {
        Integer affectedRows = lancamentoRepository.apagarTodosPorCnpjEmpresa(filter.getCnpjEmpresa());
        if (limparRegras) {
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
    public LancamentoDTO salvar(LancamentoDTO lancamentoDTO, Principal principal) throws Exception {
        Lancamento lancamento = LancamentoMapper.fromDto(lancamentoDTO);

        validaLancamento(lancamento);
        
        lancamento.setArquivo(arquivoRepository.save(
            lancamento.getArquivo().toBuilder()
                .cnpjContabilidade(lancamentoDTO.getCnpjContabilidade())
                .cnpjEmpresa(lancamentoDTO.getCnpjEmpresa())
            .build()
        ));
    

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
    public LancamentoDTO salvarTransacaoComoDePara(BigInteger id, String contaMovimento, OAuth2Authentication authentication) throws Exception {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();

        Lancamento lancamento = lancamentoRepository.save(
            this.buscarPorId(id)
            .toBuilder()
                .contaMovimento(contaMovimento)
                .tipoConta(Lancamento.TipoConta.DEPARA)
            .build()
        );

        DeParaContaDTO deParaContaDTO = DeParaContaDTO.builder()
                .cnpjContabilidade(lancamento.getCnpjContabilidade())
                .cnpjEmpresa(lancamento.getCnpjEmpresa())
                .descricao(lancamento.getDescricao())
                .contaDebito(lancamento.getTipoLancamento().equals(Lancamento.Tipo.PAGAMENTO) ? contaMovimento : null)
                .contaCredito(lancamento.getTipoLancamento().equals(Lancamento.Tipo.RECEBIMENTO) ? contaMovimento : null)
            .build();

        deParaContaClient.salvar(deParaContaDTO, "Bearer " + accessToken);

        return LancamentoMapper.fromEntity(lancamento);
    }

    public LancamentoDTO salvarTransacaoComoOutrasContas(BigInteger id, String contaMovimento, Principal principal) throws Exception {
        return LancamentoMapper.fromEntity(lancamentoRepository.save(
            buscarPorId(id)
            .toBuilder()
                .contaMovimento(contaMovimento)
                .tipoConta(Short.parseShort("2"))
            .build()
        ));
    }

    public LancamentoDTO salvarTransacaoComoIgnorar(BigInteger id, OAuth2Authentication authentication) throws Exception {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();

        Lancamento lancamento = lancamentoRepository.save(
            this.buscarPorId(id)
            .toBuilder()
                .contaMovimento("IGNORAR")
                .tipoConta(Lancamento.TipoConta.IGNORAR)
            .build()
        );

        DeParaContaDTO deParaContaDTO = DeParaContaDTO.builder()
                .cnpjContabilidade(lancamento.getCnpjContabilidade())
                .cnpjEmpresa(lancamento.getCnpjEmpresa())
                .descricao(lancamento.getDescricao())
                .contaDebito(lancamento.getTipoLancamento().equals(Lancamento.Tipo.PAGAMENTO) ? "IGNORAR" : null)
                .contaCredito(lancamento.getTipoLancamento().equals(Lancamento.Tipo.RECEBIMENTO) ? "IGNORAR" : null)
            .build();

        deParaContaClient.salvar(deParaContaDTO, "Bearer " + accessToken);

        return LancamentoMapper.fromEntity(lancamento);
    }

    //
    //
    @Transactional(rollbackFor = Exception.class) 
    public List<LancamentoDTO> importar(ImportacaoLancamentosRequest importaLancamentos, OAuth2Authentication authentication) throws Exception { 
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();

        List<Lancamento> results = new ArrayList<>();

        // Busca detalhes da empresa relacionada aos lançamento importados.
        // To-do

        // Cria o Arquivo 
        Arquivo arquivo = arquivoRepository.save(
            Arquivo.builder()
                .nome(importaLancamentos.getArquivo().getNome())
                .cnpjContabilidade(importaLancamentos.getCnpjContabilidade())
                .cnpjEmpresa(importaLancamentos.getCnpjEmpresa()).build()
        );

        // Iteração e construção de lista de lançamentos 
        List<Lancamento> lancamentos = importaLancamentos.getLancamentos().stream().map((o) -> {
            return LancamentoMapper.fromDto(o).toBuilder()
                .arquivo(arquivo)
                .cnpjContabilidade(importaLancamentos.getCnpjContabilidade())
                .cnpjEmpresa(importaLancamentos.getCnpjEmpresa())
                .idRoteiro(importaLancamentos.getIdRoteiro()).build();
        }).collect(Collectors.toList());
        
        // Iteração e validação de lista de lançamentos
        for (Lancamento lancamento : lancamentos) {
            validaLancamento(lancamento);
        }
        lancamentoRepository.saveAll(lancamentos).forEach(results::add);
        return LancamentoMapper.fromEntities(results);
    }

    private boolean validaLancamento(Lancamento lancamento) throws Exception {
        if (lancamento.getTipoLancamento() == null) {
            throw new IllegalArgumentException("Informe o tipo do lançamento!");
        }
        if (!Arrays.asList(Lancamento.Tipo.PAGAMENTO, Lancamento.Tipo.RECEBIMENTO).contains(lancamento.getTipoLancamento())) {
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