package br.com.ottimizza.integradorcloud.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
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

    public String salvar(GrupoRegra grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
        String message = "";

        GrupoRegra grupoRegra = grupoRegraRepository.save(grupoRegraDTO);

        // itera regras e adiciona lista de regras salvas
        List<Regra> regrasSalvas = new ArrayList<>();
        grupoRegraDTO.getRegras().stream().forEach((r) -> {
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
        } else if (linhasAlteradas == 1) {
            message = MessageFormat.format("{0} lançamentos afetados", linhasAlteradas);
        }

        return message;
    }

}