package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancosPadroesDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.BancosPadroesMapper;
import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;
import br.com.ottimizza.integradorcloud.repositories.BancosPadroesRepository;

@Service
public class BancosPadroesService {
    
    @Inject
    BancosPadroesRepository repository;

    public BancosPadroesDTO salva(BancosPadroes banco) throws Exception {
        BancosPadroes retorno = repository.save(banco);
        return BancosPadroesMapper.fromEntity(retorno);
    }

    public Page<BancosPadroesDTO> buscarBancos(@Valid BancosPadroesDTO filter, 
									           @Valid PageCriteria pageCriteria) throws Exception {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		Example<BancosPadroes> example = Example.of(BancosPadroesMapper.fromDTO(filter), matcher);
		return repository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize())).map(BancosPadroesMapper::fromEntity);
		
	}

    public String deletaPorId(BigInteger id) throws Exception {
        repository.deleteById(id);
        return "BancosPadroes removido com sucesso!";
    }

    public List<BancosPadroesDTO> importarBancos(List<BancosPadroes> bancos) throws Exception {
        List<BancosPadroes> retorno = new ArrayList<>();
        repository.saveAll(bancos).forEach(retorno::add);
        return BancosPadroesMapper.fromEntities(retorno);
    }
}
