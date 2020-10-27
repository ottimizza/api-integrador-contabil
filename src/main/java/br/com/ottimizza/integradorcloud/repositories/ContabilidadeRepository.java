package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import feign.Param;

@Repository
public interface ContabilidadeRepository extends JpaRepository<Contabilidade, BigInteger>{

	@Query("SELECT c FROM Contabilidade c WHERE c.cnpj = :cnpj")
	Contabilidade buscaPorCnpj(@Param("cnpj") String cnpj);
	
}
