package br.com.ottimizza.integradorcloud.repositories.arquivo;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ottimizza.integradorcloud.domain.models.Arquivo;

@Repository
public interface ArquivoRepository extends PagingAndSortingRepository<Arquivo, BigInteger> {

}