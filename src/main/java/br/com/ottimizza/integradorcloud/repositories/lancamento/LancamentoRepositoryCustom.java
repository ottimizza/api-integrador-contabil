package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;

public interface LancamentoRepositoryCustom { // @formatter:off LancamentoRepositoryImpl

    Page<Lancamento> buscarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa, Pageable pageable, Principal principal);

    @Modifying
    @Transactional
    int atualizaLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa, String contaMovimento, BigInteger regraId);
    
    @Modifying
    @Transactional
    int atualizaLancamentosPorRegraNative(List<Regra> regras, String cnpjEmpresa, String cnpjContabilidade, String contaMovimento, BigInteger regraId, Short sugerir, BigInteger regraSugerida);
    
}