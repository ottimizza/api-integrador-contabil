package br.com.ottimizza.integradorcloud.services;

import java.text.MessageFormat;

import javax.inject.Inject;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.historico.HistoricoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Historico;
import br.com.ottimizza.integradorcloud.repositories.HistoricoRepository;

@Service // @formatter:off
public class HistoricoService {

    @Inject
    HistoricoRepository historicoRepository;

    public HistoricoDTO salvar(HistoricoDTO historicoDTO, OAuth2Authentication authentication) {
        return HistoricoMapper.fromEntity(historicoRepository.save(HistoricoMapper.fromDto(historicoDTO)));
    }

    public Page<HistoricoDTO> buscar(HistoricoDTO filter, PageCriteria criteria, OAuth2Authentication authentication) throws Exception {
        Example<Historico> example = this.getDefaultQueryByExample(filter);
        return historicoRepository.findAll(example, PageCriteria.getPageRequest(criteria)).map(HistoricoMapper::fromEntity);
    }

    public HistoricoDTO buscarPorContaMovimento(String contaMovimento, String cnpjEmpresa, OAuth2Authentication authentication) {
        return HistoricoMapper.fromEntity(historicoRepository.buscarPorContaMovimento(contaMovimento, cnpjEmpresa));
    }

    public String getAuthorizationHeader(OAuth2Authentication authentication) {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();
        return MessageFormat.format("Bearer {0}", accessToken);
    }

    private Example<Historico> getDefaultQueryByExample(HistoricoDTO historicoDTO) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.EXACT);
        return Example.of(HistoricoMapper.fromDto(historicoDTO), matcher);
    }

}