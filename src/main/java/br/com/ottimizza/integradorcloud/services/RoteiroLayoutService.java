package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.RoteiroLayoutDTO;
import br.com.ottimizza.integradorcloud.repositories.RoteiroLayoutRepository;

@Service
public class RoteiroLayoutService {

    @Inject
    RoteiroLayoutRepository repository;

    public String salva(RoteiroLayoutDTO roteiroLayout) throws Exception {
        repository.inserir(roteiroLayout.getRoteiroId(), roteiroLayout.getLayoutId());
        return "Layout roteiro salvo com sucesso!";
    }

    public String deletaRoteiroLayout(BigInteger roteiroId, BigInteger layoutId) throws Exception {
        repository.deletaPorRoteiroIdELayoutId(roteiroId, layoutId);
        return "Layout removido com sucesso!";
    }
}
