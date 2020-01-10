package br.com.ottimizza.integradorcloud.repositories.regra;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ottimizza.integradorcloud.domain.models.Regra;

@Repository
public interface RegraRepository extends PagingAndSortingRepository<Regra, BigInteger> {// , LancamentoRepositoryCustom
                                                                                        // {

}