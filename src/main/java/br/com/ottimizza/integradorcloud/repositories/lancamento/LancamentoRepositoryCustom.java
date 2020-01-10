package br.com.ottimizza.integradorcloud.repositories.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

public interface LancamentoRepositoryCustom { // LancamentoRepositoryImpl

    Page<Lancamento> fetchAll(LancamentoDTO filter, Pageable pageRequest);

    long deleteAll(LancamentoDTO filter);

}