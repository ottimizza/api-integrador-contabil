package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.RoteiroLayoutDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.RoteiroLayoutMapper;
import br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts.RoteiroLayout;
import br.com.ottimizza.integradorcloud.repositories.RoteiroLayoutRepository;

@Service
public class RoteiroLayoutService {

    @Inject
    RoteiroLayoutRepository repository;

    public RoteiroLayoutDTO salva(RoteiroLayout roteiroLayout) throws Exception {
        return RoteiroLayoutMapper.fromEntity(repository.save(roteiroLayout));
    }

    public String deletaRoteiroLayout(BigInteger roteiroId, BigInteger layoutId) throws Exception {
        repository.deletaPorRoteiroIdELayoutId(roteiroId, layoutId);
        return "Layout removido com sucesso!";
    }
}
