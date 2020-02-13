package br.com.ottimizza.integradorcloud.repositories.arquivo;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.Arquivo;

@Repository
public interface ArquivoRepository extends PagingAndSortingRepository<Arquivo, BigInteger> {

	@Query(value = " select * from arquivos a2 where a2.cnpj_contabilidade = :cnpjContabilidade	and"
				 + " a2.cnpj_empresa = :cnpjEmpresa	and a.nome like :nome and a2.data_criacao  >= now()::date" ,nativeQuery = true)
	Optional<Arquivo> findByNomeCnpjs(@Param("cnpjEmpresa") String cnpjEmpresa, @Param("cnpjContabilidade") String cnpjContabildade, @Param("nome") String nome);
	
	
}