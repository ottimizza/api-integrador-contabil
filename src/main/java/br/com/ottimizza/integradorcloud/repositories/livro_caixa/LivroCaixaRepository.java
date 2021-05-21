package br.com.ottimizza.integradorcloud.repositories.livro_caixa;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

@Repository
public interface LivroCaixaRepository extends JpaRepository<LivroCaixa, BigInteger>, LivroCaixaRepositoryCustom{

	@Query(value =	 " SELECT * FROM livros_caixas lc					"
			+"		WHERE lc.cnpj_contabilidade = :cnpjContabilidade	"
			+"		AND   lc.cnpj_empresa = :cnpjEmpresa				"
			+"		ORDER BY lc.id DESC LIMIT 1							", nativeQuery = true)
	LivroCaixa findByCnpjContabilidadeAndCnpjEmpresaFirstByOrderByIdDesc(@Param("cnpjContabilidade") String cnpjContabilidade, 
																		 @Param("cnpjEmpresa") String cnpjEmpresa);

    @Query(value ="SELECT SUM(lc.valor_original) "
	 			 +"FROM livros_caixas lc "
				 +"WHERE lc.cnpj_empresa = :cnpjEmpresa "
				 +"AND lc.tipo_movimento = 'PAG' "
				 +"AND lc.status = 0  "
				 +"AND lc.data_movimento <= :dataMovimento ", nativeQuery = true)
	Double buscaPagamentosPendentes(@Param("cnpjEmpresa") String cnpjEmpresa,
									@Param("dataMovimento") LocalDate dataMovimento);

	@Query(value ="SELECT SUM(lc.valor_original) "
				 +"FROM livros_caixas lc "
				 +"WHERE lc.cnpj_empresa = :cnpjEmpresa "
				 +"AND lc.tipo_movimento = 'REC' "
				 +"AND lc.status = 0  "
				 +"AND lc.data_movimento <= :dataMovimento ", nativeQuery = true)
    Double buscaRecebimentosPendentes(@Param("cnpjEmpresa") String cnpjEmpresa,
									  @Param("dataMovimento") LocalDate dataMovimento);
				  

	@Query(value = "SELECT lc.* FROM livros_caixas lc WHERE lc.id_externo = :idExterno ", nativeQuery = true)
	LivroCaixa findByIdExterno(@Param("idExterno") String idExterno);

	@Query(value = "SELECT lc.* FROM livros_caixas lc WHERE lc.cnpj_empresa = :cnpjEmpresa AND lc.fk_banco_id = :bancoId AND data_movimento <= :data AND lc.integrado_contabilidade = false", nativeQuery = true)
	List<LivroCaixa> findByCnpjEmpresaBancoData(@Param("cnpjEmpresa") String cnpjEmpresa, @Param("bancoId") BigInteger bancoId, @Param("data") LocalDate data);
}
