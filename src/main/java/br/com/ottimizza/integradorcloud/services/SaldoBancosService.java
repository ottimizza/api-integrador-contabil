package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.SaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.SaldoBancosMapper;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;
import br.com.ottimizza.integradorcloud.repositories.SaldoBancosRepository;

@Service
public class SaldoBancosService {
    
    @Inject
    SaldoBancosRepository repository;

    public SaldoBancos salvaSaldo(SaldoBancosDTO saldoDTO) throws Exception {
        SaldoBancos saldoBanco = repository.buscaPorBancoData(saldoDTO.getBancoId(), saldoDTO.getData());
        if(saldoBanco != null){
            saldoDTO.setId(saldoBanco.getId());
        }
        return repository.save(SaldoBancosMapper.fromDTO(saldoDTO));
    }

    public SaldoBancosDTO buscaUltimoSaldo(BigInteger bancoId) throws Exception {
        //return SaldoBancosMapper.fromEntity(repository.buscaUltimoSaldoPorBanco(bancoId));
        return null;
    }

    public String deletaSaldo(BigInteger id) throws Exception {
        repository.deleteById(id);
        return "SaldoBanco removido com sucesso!";
    }

}
