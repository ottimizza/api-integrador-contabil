package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;

public interface LancamentoRepositoryCustom { // @formatter:off

    Page<Lancamento> buscarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa, Pageable pageable, Principal principal);

}