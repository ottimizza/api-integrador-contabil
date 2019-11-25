package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;

@Service
public class LancamentoService {

    public List<LancamentoDTO> fetchAll() throws Exception {
        return Arrays.asList(new LancamentoDTO[] {});
    }

    public LancamentoDTO fetchById(BigInteger id) throws Exception {
        return new LancamentoDTO();
    }

}