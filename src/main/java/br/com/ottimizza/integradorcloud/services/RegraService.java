package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.h2.util.json.JSONArray;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra_ignorada.GrupoRegraIgnoradaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.user.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegraIgnorada;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra_ignorada.GrupoRegraIgnoradaRepository;
import br.com.ottimizza.integradorcloud.repositories.lancamento.LancamentoRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;
import br.com.ottimizza.integradorcloud.utils.DateUtils;

@Service // @formatter:off
public class RegraService {

    @Inject
    LancamentoRepository lancamentoRepository;

    @Inject
    GrupoRegraRepository grupoRegraRepository;

    @Inject
    GrupoRegraIgnoradaRepository grupoRegraIgnoradaRepository;
    
    @Inject
    RegraRepository regraRepository;

    @Inject 
    OAuthClient oauthClient;
    
    public Page<GrupoRegraDTO> buscarRegras(GrupoRegraDTO filtro, PageCriteria pageCriteria, OAuth2Authentication authentication) 
            throws Exception {
        filtro.setAtivo(true);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<GrupoRegra> example = Example.of(GrupoRegraMapper.fromDto(filtro), matcher);

        // sot by fixo
        Sort sort = Sort.by( // contagemRegras , posicao
            Sort.Order.asc("contagemRegras"),
            Sort.Order.asc("posicao")
        );

        // PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), sort);
        
        return grupoRegraRepository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize(), sort))
                            .map((grupoRegra) -> {
                                GrupoRegraDTO grupoRegraDTO = GrupoRegraMapper.fromEntity(grupoRegra);
                                grupoRegraDTO.setRegras(regraRepository.buscarPorGrupoRegra(grupoRegra.getId()));
                                return grupoRegraDTO;
                            });
    }

    public GrupoRegraDTO salvar(GrupoRegraDTO grupoRegraDTO, Short sugerir, String regraSugerida, OAuth2Authentication authentication) throws Exception {
    	List<String> campos = new ArrayList();
    	BigInteger regraId = null;
        validaGrupoRegra(grupoRegraDTO);
        grupoRegraDTO.setUsuario(authentication.getName());

        grupoRegraDTO.setPosicao(grupoRegraRepository.buscarUltimaPosicaoPorEmpresaETipoLancamento(
            grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getTipoLancamento()
        ));
        grupoRegraDTO.setPosicao(grupoRegraDTO.getPosicao() == null ? 1 : grupoRegraDTO.getPosicao() + 1);
        grupoRegraDTO = validarGrupoRegra(grupoRegraDTO);
        
        GrupoRegra grupoRegra = grupoRegraRepository.save(GrupoRegraMapper.fromDto(grupoRegraDTO));
        List<Regra> regrasSalvas = salvarRegras(grupoRegra, grupoRegraDTO.getRegras());
        ajustarPesoRegra(grupoRegra.getId());
        
        
        grupoRegraDTO = GrupoRegraMapper.fromEntity(grupoRegra);
        grupoRegraDTO.setRegras(regrasSalvas);
        
        if(!regraSugerida.equals("") && !regraSugerida.equals("null"))
        	regraId = BigInteger.valueOf(Integer.parseInt(regraSugerida));
        
        lancamentoRepository.atualizaLancamentosPorRegraNative(
            regrasSalvas, grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getCnpjContabilidade(), grupoRegraDTO.getContaMovimento(), grupoRegra.getId(), sugerir, regraId);

        grupoRegraRepository.ajustePosicao(grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getCnpjContabilidade(), grupoRegraDTO.getTipoLancamento());
        return grupoRegraDTO;
    }

    public GrupoRegraDTO atualizar(BigInteger id, GrupoRegraDTO grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
    	List<String> campos = new ArrayList();
        GrupoRegra existente = grupoRegraRepository.findById(id).orElseThrow(() -> new NoResultException("Regra não encontrada!"));
        grupoRegraDTO.setUsuario(authentication.getName());

        grupoRegraDTO.setPosicao(existente.getPosicao());
        grupoRegraDTO.setTipoLancamento(existente.getTipoLancamento());
        grupoRegraDTO.setIdRoteiro(existente.getIdRoteiro());
        grupoRegraDTO.setCnpjEmpresa(existente.getCnpjEmpresa());
        grupoRegraDTO.setCnpjContabilidade(existente.getCnpjContabilidade());
        grupoRegraDTO.setAtivo(existente.getAtivo());
        
        validaGrupoRegra(grupoRegraDTO);

        grupoRegraDTO.setDataCriacao(DateUtils.toLocalDateTime(existente.getDataCriacao()));

        if (Objects.isNull(grupoRegraDTO.getPosicao()) || grupoRegraDTO.getPosicao() < 0) {
            throw new IllegalArgumentException("Informe a posição da regra!");
        }

        grupoRegraDTO.setId(id);
        grupoRegraDTO = validarGrupoRegra(grupoRegraDTO);
        GrupoRegra grupoRegra = grupoRegraRepository.save(GrupoRegraMapper.fromDto(grupoRegraDTO));

        regraRepository.apagarPorGrupoRegra(id);
        List<Regra> regrasSalvas = salvarRegras(grupoRegra, grupoRegraDTO.getRegras());
        ajustarPesoRegra(grupoRegra.getId());

        grupoRegraDTO = GrupoRegraMapper.fromEntity(grupoRegra);
        grupoRegraDTO.setRegras(regrasSalvas);
        
        if(grupoRegraDTO.getContagemRegras() != existente.getContagemRegras())
        	grupoRegraRepository.ajustePosicao(grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getCnpjContabilidade(), grupoRegraDTO.getTipoLancamento());
        
        return grupoRegraDTO;
    }
    
    public GrupoRegraDTO clonar(BigInteger id, OAuth2Authentication authentication) throws Exception {
    	List<String> campos = new ArrayList();
    	GrupoRegra existente = grupoRegraRepository.findById(id).orElseThrow(() -> new NoResultException("Regra não encontrada!"));
    	List<Regra> regrasExistentes = regraRepository.buscarPorGrupoRegra(id);
    	
    	GrupoRegraDTO grupoRegraDto = GrupoRegraDTO.builder()
    			.posicao(existente.getPosicao())
    			.contaMovimento(existente.getContaMovimento())
    			.tipoLancamento(existente.getTipoLancamento())
    			.idRoteiro(existente.getIdRoteiro())
    			.cnpjEmpresa(existente.getCnpjEmpresa())
    			.cnpjContabilidade(existente.getCnpjContabilidade())
    			.contagemRegras(existente.getContagemRegras())
                .usuario(authentication.getName())
                .camposRegras(existente.getCamposRegras())
    		.build();
    	GrupoRegra grupoRegra = grupoRegraRepository.save(GrupoRegraMapper.fromDto(grupoRegraDto));
    	List<Regra> regras = regrasExistentes.stream().map((Regra r) -> {
    		return regraRepository.save(r.toBuilder().id(null).grupoRegra(grupoRegra).build());
    	}).collect(Collectors.toList()); 
    	ajustarPesoRegra(grupoRegra.getId());
    	grupoRegraRepository.ajustePosicao(grupoRegra.getCnpjEmpresa(), grupoRegra.getCnpjContabilidade(), grupoRegra.getTipoLancamento());
    	
    	return GrupoRegraMapper.fromEntity(grupoRegra).toBuilder().regras(regras).build();
    }

    public String apagar(BigInteger id, OAuth2Authentication authentication) throws Exception {
    	 GrupoRegra grupoRegra = grupoRegraRepository.findById(id)
    	            .orElseThrow(() -> new NoResultException("Regra não encontrada!"));
    	
        //regraRepository.apagarPorGrupoRegra(id);
        //grupoRegraRepository.deleteById(id);
        grupoRegraRepository.inativarGrupoRegra(id, authentication.getName());
        lancamentoRepository.restaurarPorRegraId(id, grupoRegra.getCnpjEmpresa());
        ajustarPesoRegra(id);
        
        grupoRegraRepository.ajustePosicao(grupoRegra.getCnpjEmpresa(), grupoRegra.getCnpjContabilidade(), grupoRegra.getTipoLancamento());
        return "Grupo de Regra removido com sucesso!";
    }

    public GrupoRegraDTO alterarPosicao(BigInteger id, Integer posicaoAtual, OAuth2Authentication authentication) throws Exception {
        GrupoRegra grupoRegra = grupoRegraRepository.findById(id)
            .orElseThrow(() -> new NoResultException("Regra não encontrada!"));
        Integer posicaoAnterior = grupoRegra.getPosicao();

        // Quando regra é movida para baixo (final).
        // decrementa os indices no intervalo.
        if (posicaoAtual > posicaoAnterior) {
            grupoRegraRepository.decrementaPosicaoPorIntervalo(
                grupoRegra.getCnpjEmpresa(), grupoRegra.getTipoLancamento(), posicaoAnterior, posicaoAtual);
            grupoRegraRepository.atualizaPosicaoPorId(id, posicaoAtual);
            grupoRegra.setPosicao(posicaoAtual);
        // Quando regra é movida para cima (inicio).
        // incrementa os indices no intervalo.
        } else if (posicaoAtual < posicaoAnterior) {
            grupoRegraRepository.incrementaPosicaoPorIntervalo(
                grupoRegra.getCnpjEmpresa(), grupoRegra.getTipoLancamento(), posicaoAnterior, posicaoAtual);
            grupoRegraRepository.atualizaPosicaoPorId(id, posicaoAtual);
            grupoRegra.setPosicao(posicaoAtual);
        }
        grupoRegraRepository.ajustePosicao(grupoRegra.getCnpjEmpresa(), grupoRegra.getCnpjContabilidade(), grupoRegra.getTipoLancamento());
        return GrupoRegraMapper.fromEntity(grupoRegra);
    }

    public GrupoRegraDTO moverRegraParaInicio(BigInteger id, OAuth2Authentication authentication) throws Exception {
        return this.alterarPosicao(id, 0, authentication);
    }

    public GrupoRegraDTO moverRegraParaFinal(BigInteger id, OAuth2Authentication authentication) throws Exception {
        GrupoRegra grupoRegra = grupoRegraRepository.findById(id)
            .orElseThrow(() -> new NoResultException("Regra não encontrada!"));
        Integer ultima = grupoRegraRepository.buscarUltimaPosicaoPorEmpresaETipoLancamento(
            grupoRegra.getCnpjEmpresa(), grupoRegra.getTipoLancamento()
        );
        if (ultima > 1) {
            return this.alterarPosicao(id, ultima + 1, authentication);
        }
        return GrupoRegraMapper.fromEntity(grupoRegra); 
    }


    private List<Regra> salvarRegras(GrupoRegra grupo, List<Regra> regras) {
        return regras.stream().map((Regra regra) -> {
            // adiciona referencia ao grupo, cria regra no banco de dados e retorna o objeto atualizado.
            return regraRepository.save(regra.toBuilder().grupoRegra(grupo).build());
        }).collect(Collectors.toList());
    }
    
    private void ajustarPesoRegra(BigInteger grupoRegraId) throws Exception {
    	grupoRegraRepository.ajustarPesoRegraUnica(grupoRegraId);
    	grupoRegraRepository.ajustarPesoRegras(grupoRegraId);
    }

    private boolean validaGrupoRegra(GrupoRegraDTO grupoRegraDTO) throws IllegalArgumentException {
        String idRoteiro = grupoRegraDTO.getIdRoteiro();
        String contaMovimento = grupoRegraDTO.getContaMovimento();
        Short tipoLancamento = grupoRegraDTO.getTipoLancamento();
        String cnpjEmpresa = grupoRegraDTO.getCnpjEmpresa();
        String cnpjContabilidade = grupoRegraDTO.getCnpjContabilidade();

        if (Objects.isNull(cnpjContabilidade) || cnpjContabilidade.isEmpty()) {
            throw new IllegalArgumentException("Informe o CNPJ da Contabilidade!");
        }
        if (Objects.isNull(cnpjEmpresa) || cnpjEmpresa.isEmpty()) {
            throw new IllegalArgumentException("Informe o CNPJ da Empresa!");
        }
        if (Objects.isNull(contaMovimento) || contaMovimento.isEmpty()) {
            throw new IllegalArgumentException("Informe a Conta Movimento!");
        }
        if (!Arrays.asList(Lancamento.Tipo.PAGAMENTO, Lancamento.Tipo.RECEBIMENTO).contains(tipoLancamento)) {
            throw new IllegalArgumentException("Informe o Tipo de Lançamento! Pagamentos(1) ou Recebimentos(2).");
        }
        if (Objects.isNull(idRoteiro) || idRoteiro.isEmpty()) {
            throw new IllegalArgumentException("Informe a Id do Roteiro!");
        }
        return true;
    }
    
    public List<GrupoRegra> findToSalesForce(GrupoRegraDTO filtro, OAuth2Authentication authentication) 
            throws Exception {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<GrupoRegra> example = Example.of(GrupoRegraMapper.fromDto(filtro), matcher);
        
        return grupoRegraRepository.findAll(example);
    }
    
    public GrupoRegraDTO sugerirRegra(String cnpjContabilidade, Short busca, BigInteger lancamentoId) throws Exception {
    	GrupoRegra grupoRegra = new GrupoRegra();
    	try {
    		grupoRegra = grupoRegraRepository.sugerirRegra(busca, lancamentoId, cnpjContabilidade);
    		try {
    			GrupoRegra grupoRegraContabilidade = grupoRegraRepository.buscarPorCamposContabilidade(cnpjContabilidade, grupoRegra.getId());
        		if(grupoRegraContabilidade != null)
        			return GrupoRegraMapper.fromEntity(grupoRegraContabilidade);
    		}catch(Exception ex) { }
    		if(!grupoRegra.getCnpjContabilidade().equals(cnpjContabilidade))
    			grupoRegra.setContaMovimento(null);
    		
    		return GrupoRegraMapper.fromEntity(grupoRegra);
    	}catch(Exception ex) {
    		System.out.println("");
    		return null;
    	}
    	
    }
    
    public GrupoRegraIgnoradaDTO ignorarSugestaoRegra(GrupoRegraIgnoradaDTO grupoRegra, OAuth2Authentication authentication) throws Exception {
    	UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody().getRecord();
    	grupoRegra.setCnpjContabilidade(userInfo.getOrganization().getCnpj());
    	GrupoRegraIgnorada regraIgnorada = grupoRegraIgnoradaRepository.save(GrupoRegraMapper.ignoradaFromDto(grupoRegra));
    	return GrupoRegraMapper.ignoradaFromEntity(regraIgnorada);
    }

    public GrupoRegraDTO validarGrupoRegra(GrupoRegraDTO grupoRegra) throws Exception {
    	List<String> campos = new ArrayList<>();
    	Boolean portador = false;
    	Boolean naoContem = false;
    	Boolean emBranco = false;
    	int contagemRegras = 0;
        for(Regra r : grupoRegra.getRegras()) {
        	if(!r.getCampo().equals("tipoPlanilha")) {
        		campos.add(r.getValor().trim());
        		String[] array = r.getValor().trim().split(" ");
        		contagemRegras = contagemRegras + contarValoresComPeso(array, r.getCampo());
        	}
        	
        	if(r.getCampo().equals("portador"))
        		portador = true;
        	
        	if(r.getCondicao() == 2)
        		naoContem = true;
        	
        	if(r.getValor().equals("EM BRANCO"))
        		emBranco = true;
        }
        
        
        if(naoContem) {
        	campos = new ArrayList<>();
        	campos.add("NULL");
        }
        else if(emBranco) {
        	campos = new ArrayList<>();
        	campos.add("NULL");
        	//grupoRegra.setCamposRegras(new ArrayList<>());
        }
        else if(portador) {
        	campos = new ArrayList<>();
        	campos.add("NULL");
        	//grupoRegra.setCamposRegras(new ArrayList<>());
        }
        else if(contagemRegras < 3) {
        	campos = new ArrayList<>();
        	campos.add("NULL");
    		//grupoRegra.setCamposRegras(new ArrayList<String>().add("NULL"));
        }
        else if(grupoRegra.getContaMovimento().equals("IGNORAR")) {
        	campos = new ArrayList<>();
        	campos.add("NULL");
    		//grupoRegra.setCamposRegras(new ArrayList<>());
        }
        grupoRegra.setContagemRegras(contagemRegras);
        grupoRegra.setCamposRegras(campos);
        
    	return grupoRegra;
    }
    
    public int contarValoresComPeso(String[] campos, String campo) throws Exception {
    	int contagemRegras = 0;
    	if(campo.contains("FGTS"))
    			contagemRegras = contagemRegras + 5;
    	if(campo.contains("RESCISORIO"))
    			contagemRegras = contagemRegras + 5;
		contagemRegras = contagemRegras + campos.length;
    	return contagemRegras;
    }
    
    private String getAuthorizationHeader(OAuth2Authentication authentication) {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();
        return MessageFormat.format("Bearer {0}", accessToken);
    }

}

