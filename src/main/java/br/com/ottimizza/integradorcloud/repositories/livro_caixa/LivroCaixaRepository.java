package br.com.ottimizza.integradorcloud.repositories.livro_caixa;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

@Repository
public interface LivroCaixaRepository extends JpaRepository<LivroCaixa, BigInteger>, LivroCaixaRepositoryCustom{

	@Query(value =	 " SELECT * FROM livros_caixas lc							"
			+"		WHERE lc.cnpj_contabilidade = :cnpjContabilidade	"
			+"		AND   lc.cnpj_empresa = :cnpjEmpresa				"
			+"		ORDER BY lc.id DESC LIMIT 1							", nativeQuery = true)
	LivroCaixa findByCnpjContabilidadeAndCnpjEmpresaFirstByOrderByIdDesc(@Param("cnpjContabilidade") String cnpjContabilidade, 
																		 @Param("cnpjEmpresa") String cnpjEmpresa);


}
