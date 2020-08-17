package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

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
    
    public List<Historico> buscaHistoricosProSF(HistoricoDTO filter, OAuth2Authentication authentication) throws Exception {
    	return historicoRepository.buscaHistoricosParaSalesForce(filter.getCnpjContabilidade(), filter.getCnpjEmpresa(), filter.getTipoLancamento());
    }

    public HistoricoDTO buscarPorContaMovimento(String contaMovimento, String cnpjEmpresa, Short tipoLancamento, OAuth2Authentication authentication) {
        return HistoricoMapper.fromEntity(historicoRepository.buscarPorContaMovimento(contaMovimento, cnpjEmpresa, tipoLancamento));
    }
    
    public String deletaPorId(BigInteger id) throws Exception {
    	historicoRepository.deleteById(id);
    	return "Historico removido com sucesso!";
    }
    
    public HistoricoDTO atualizar(BigInteger id, HistoricoDTO historico, OAuth2Authentication authentication) throws Exception {
    	Historico existente = historicoRepository.findById(id).orElseThrow(() -> new NoResultException("Historico não encontrada!"));
    	historico.setCnpjContabilidade(existente.getCnpjContabilidade());
    	historico.setCnpjEmpresa(existente.getCnpjEmpresa());
    	historico.setContaMovimento(existente.getContaMovimento());
    	historico.setTipoLancamento(existente.getTipoLancamento());
    	historico.setIdRoteiro(existente.getIdRoteiro());
    	historico.setDataCriacao(existente.getDataCriacao());
    	
    	return HistoricoMapper.fromEntity(historicoRepository.save(HistoricoMapper.fromDto(historico)));
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