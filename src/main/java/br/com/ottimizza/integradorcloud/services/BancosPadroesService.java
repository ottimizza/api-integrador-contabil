package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancosPadroesDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.BancosPadroesMapper;
import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;
import br.com.ottimizza.integradorcloud.repositories.BancosPadroesRepository;

@Service
public class BancosPadroesService {
    
    @Inject
    BancosPadroesRepository repository;

    public BancosPadroesDTO salva(BancosPadroesDTO banco) throws Exception {
        BancosPadroes retorno = repository.save(BancosPadroesMapper.fromDTO(banco));
        return BancosPadroesMapper.fromEntity(retorno);
    }

    public String deletaPorId(BigInteger id) throws Exception {
        repository.deleteById(id);
        return "BancosPadroes removido com sucesso!";
    }
}
