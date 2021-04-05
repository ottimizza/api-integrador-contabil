package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.HistoricoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Historico;
import br.com.ottimizza.integradorcloud.repositories.HistoricoRepository;

@Service // @formatter:off
public class HistoricoService {

    @Inject
    HistoricoRepository historicoRepository;

    public HistoricoDTO salvar(HistoricoDTO historicoDTO, OAuth2Authentication authentication) throws Exception {
        historicoDTO.setUsuario(authentication.getName());
    	Historico historico = HistoricoMapper.fromDto(historicoDTO);
    	validaHistorico(historico);
        return HistoricoMapper.fromEntity(historicoRepository.save(historico));
    }

    public Page<HistoricoDTO> buscar(HistoricoDTO filter, PageCriteria criteria, OAuth2Authentication authentication) throws Exception {
        filter.setAtivo(true);
        Example<Historico> example = this.getDefaultQueryByExample(filter);
        Sort sort = Sort.by(
        		Sort.Order.asc("dataCriacao")
        	);
        return historicoRepository.findAll(example, PageRequest.of(criteria.getPageIndex(), criteria.getPageSize(), sort)).map(HistoricoMapper::fromEntity);
    }
    
    public List<Historico> buscaHistoricosProSF(HistoricoDTO filter, OAuth2Authentication authentication) throws Exception {
    	return historicoRepository.buscaHistoricosParaSalesForce(filter.getCnpjContabilidade(), filter.getCnpjEmpresa(), filter.getTipoLancamento());
    }

    public HistoricoDTO buscarPorContaMovimento(String contaMovimento, String cnpjEmpresa, Short tipoLancamento, OAuth2Authentication authentication) {
        return HistoricoMapper.fromEntity(historicoRepository.buscarPorContaMovimento(contaMovimento, cnpjEmpresa, tipoLancamento));
    }
    
    public String deletaPorId(BigInteger id, OAuth2Authentication authentication) throws Exception {
    	historicoRepository.inativarHistorico(id, authentication.getName());
        //historicoRepository.deleteById(id);
    	return "Historico removido com sucesso!";
    }
    
    public HistoricoDTO atualizar(BigInteger id, HistoricoDTO historicoDTO, OAuth2Authentication authentication) throws Exception {
    	Historico existente = historicoRepository.findById(id).orElseThrow(() -> new NoResultException("Historico não encontrada!"));
    	Historico historico = HistoricoMapper.fromDto(historicoDTO);
    	validaHistorico(historico);
    	
    	historico.setCnpjContabilidade(existente.getCnpjContabilidade());
    	historico.setCnpjEmpresa(existente.getCnpjEmpresa());
    	historico.setContaMovimento(existente.getContaMovimento());
    	historico.setTipoLancamento(existente.getTipoLancamento());
    	historico.setIdRoteiro(existente.getIdRoteiro());
    	historico.setDataCriacao(existente.getDataCriacao());
    	historico.setUsuario(authentication.getName());
    	
    	return HistoricoMapper.fromEntity(historicoRepository.save(historico));
    }

    private Example<Historico> getDefaultQueryByExample(HistoricoDTO historicoDTO) {
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.EXACT);
        return Example.of(HistoricoMapper.fromDto(historicoDTO), matcher);
    }
    
    public boolean validaHistorico(Historico historico) throws Exception {
    	if(historico.getContaMovimento() == null || historico.getContaMovimento().equals(""))
    		throw new IllegalArgumentException("Informe a conta movimento relacionada ao historico!");
    	if(historico.getHistorico() == null || historico.getHistorico().equals(""))
    		throw new IllegalArgumentException("Informe a regra do historico!");
    	if(historico.getCnpjEmpresa() == null || historico.getCnpjEmpresa().equals(""))
    		throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao historico!");
    	if(historico.getCnpjContabilidade() == null || historico.getCnpjContabilidade().equals(""))
       		throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao historico!");
    	if(historico.getTipoLancamento() == null)
    		throw new IllegalArgumentException("Informe o tipo de lancamento relacionado ao historico!");
    	if(historico.getIdRoteiro() == null || historico.getIdRoteiro().equals(""))
    		throw new IllegalArgumentException("Informe o id de roteiro relacionado ao historico!");
    	return true;
    }


}