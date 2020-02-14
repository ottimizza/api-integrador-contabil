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

	@Query(value = 
		   " SELECT * FROM arquivos a2 									" 
		  +"		WHERE a2.cnpj_contabilidade = :cnpjContabilidade	" 
		  +"		AND a2.cnpj_empresa = :cnpjEmpresa					" 
		  +"		AND a2.nome = :nome 								"
			,nativeQuery = true)
	Arquivo findArquivo(@Param("cnpjEmpresa") String cnpjEmpresa, @Param("cnpjContabilidade") String cnpjContabildade, @Param("nome") String nome);
	
	
}