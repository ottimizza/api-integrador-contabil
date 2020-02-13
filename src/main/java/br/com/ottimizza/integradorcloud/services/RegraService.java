package br.com.ottimizza.integradorcloud.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public List<GrupoRegraDTO> buscarRegras(GrupoRegraDTO filtro, PageCriteria pageCriteria, OAuth2Authentication authentication) 
            throws Exception {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<GrupoRegra> example = Example.of(GrupoRegraMapper.fromDto(filtro), matcher);

        Page<GrupoRegra> grupoRegras = grupoRegraRepository.findAll(example, PageCriteria.getPageRequest(pageCriteria));

        return new ArrayList<>();
    }

    public String salvar(GrupoRegraDTO grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
        String message = "";

        validaGrupoRegra(grupoRegraDTO);

        Integer posicao = grupoRegraRepository.buscarUltimaPosicaoPorEmpresaETipoLancamento(
            grupoRegraDTO.getCnpjEmpresa(), grupoRegraDTO.getTipoLancamento()
        );

        List<Regra> regras = grupoRegraDTO.getRegras();

        GrupoRegra grupoRegra = grupoRegraRepository.save(GrupoRegraMapper.fromDto(grupoRegraDTO));

        System.out.println("Count >> " + regras.size());

        // itera regras e adiciona lista de regras salvas
        List<Regra> regrasSalvas = new ArrayList<>();
        regras.stream().forEach((r) -> {
            r.setGrupoRegra(grupoRegra);
            regrasSalvas.add(regraRepository.save(r));
        });

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

    boolean validaGrupoRegra(GrupoRegraDTO grupoRegraDTO) throws IllegalArgumentException {
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