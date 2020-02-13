package br.com.ottimizza.integradorcloud.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.lancamento.LancamentoRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;

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

    public String salvar(GrupoRegraDTO grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
        String message = "";

        validaGrupoRegra(grupoRegraDTO);

        grupoRegraDTO.setPosicao(grupoRegraRepository.buscarUltimaPosicaoPorEmpresaETipoLancamento(
            grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getTipoLancamento()
        ));
        grupoRegraDTO.setPosicao(grupoRegraDTO.getPosicao() == null ? 1 : grupoRegraDTO.getPosicao() + 1);

        GrupoRegra grupoRegra = grupoRegraRepository.save(GrupoRegraMapper.fromDto(grupoRegraDTO));
        List<Regra> regrasSalvas = salvarRegras(grupoRegra, grupoRegraDTO.getRegras());

        // atualiza lançamentos baseados na regra.
        int linhasAlteradas = lancamentoRepository.atualizaLancamentosPorRegra(
            regrasSalvas, grupoRegra.getCnpjEmpresa(), grupoRegra.getContaMovimento()
        );

        if (linhasAlteradas == 0) {
            message = MessageFormat.format("Nenhum lançamento afetado!", linhasAlteradas);
        } else if (linhasAlteradas == 1) {
            message = MessageFormat.format("Um lançamento afetado!", linhasAlteradas);
        } else if (linhasAlteradas > 1) {
            message = MessageFormat.format("{0} lançamentos afetados", linhasAlteradas);
        }

        return message;
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

}

