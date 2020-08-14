package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
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
    RegraRepository regraRepository;

    public Page<GrupoRegraDTO> buscarRegras(GrupoRegraDTO filtro, PageCriteria pageCriteria, OAuth2Authentication authentication) 
            throws Exception {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<GrupoRegra> example = Example.of(GrupoRegraMapper.fromDto(filtro), matcher);
        
        return grupoRegraRepository.findAll(example, PageCriteria.getPageRequest(pageCriteria))
                            .map((grupoRegra) -> {
                                GrupoRegraDTO grupoRegraDTO = GrupoRegraMapper.fromEntity(grupoRegra);
                                grupoRegraDTO.setRegras(regraRepository.buscarPorGrupoRegra(grupoRegra.getId()));
                                return grupoRegraDTO;
                            });
    }

    public GrupoRegraDTO salvar(GrupoRegraDTO grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
        validaGrupoRegra(grupoRegraDTO);

        grupoRegraDTO.setPosicao(grupoRegraRepository.buscarUltimaPosicaoPorEmpresaETipoLancamento(
            grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getTipoLancamento()
        ));
        grupoRegraDTO.setPosicao(grupoRegraDTO.getPosicao() == null ? 1 : grupoRegraDTO.getPosicao() + 1);
        grupoRegraDTO.setContagemRegras(grupoRegraDTO.getRegras().size());
        
        GrupoRegra grupoRegra = grupoRegraRepository.save(GrupoRegraMapper.fromDto(grupoRegraDTO));
        List<Regra> regrasSalvas = salvarRegras(grupoRegra, grupoRegraDTO.getRegras());

        grupoRegraDTO = GrupoRegraMapper.fromEntity(grupoRegra);
        grupoRegraDTO.setRegras(regrasSalvas);

        lancamentoRepository.atualizaLancamentosPorRegra(
            regrasSalvas, grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getContaMovimento());

        return grupoRegraDTO;
    }

    public GrupoRegraDTO atualizar(BigInteger id, GrupoRegraDTO grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
        GrupoRegra existente = grupoRegraRepository.findById(id).orElseThrow(() -> new NoResultException("Regra não encontrada!"));
        
        grupoRegraDTO.setPosicao(existente.getPosicao());
        grupoRegraDTO.setTipoLancamento(existente.getTipoLancamento());
        grupoRegraDTO.setIdRoteiro(existente.getIdRoteiro());
        grupoRegraDTO.setCnpjEmpresa(existente.getCnpjEmpresa());
        grupoRegraDTO.setCnpjContabilidade(existente.getCnpjContabilidade());
        
        validaGrupoRegra(grupoRegraDTO);

        grupoRegraDTO.setDataCriacao(DateUtils.toLocalDateTime(existente.getDataCriacao()));

        if (Objects.isNull(grupoRegraDTO.getPosicao()) || grupoRegraDTO.getPosicao() < 0) {
            throw new IllegalArgumentException("Informe a posição da regra!");
        }

        grupoRegraDTO.setId(id);
        grupoRegraDTO.setContagemRegras(grupoRegraDTO.getRegras().size()); 
        GrupoRegra grupoRegra = grupoRegraRepository.save(GrupoRegraMapper.fromDto(grupoRegraDTO));

        regraRepository.apagarPorGrupoRegra(id);
        List<Regra> regrasSalvas = salvarRegras(grupoRegra, grupoRegraDTO.getRegras());

        grupoRegraDTO = GrupoRegraMapper.fromEntity(grupoRegra);
        grupoRegraDTO.setRegras(regrasSalvas);

        return grupoRegraDTO;
    }

    public String apagar(BigInteger id, OAuth2Authentication authentication) throws Exception {

        regraRepository.apagarPorGrupoRegra(id);

        grupoRegraRepository.deleteById(id);

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


}

