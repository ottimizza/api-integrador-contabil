package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

@Repository
public interface LancamentoRepository extends PagingAndSortingRepository<Lancamento, BigInteger> {

}