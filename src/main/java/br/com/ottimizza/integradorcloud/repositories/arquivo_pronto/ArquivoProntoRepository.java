package br.com.ottimizza.integradorcloud.repositories.arquivo_pronto;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;

@Repository
public interface ArquivoProntoRepository extends JpaRepository<ArquivoPronto, BigInteger>, ArquivoProntoRepositoryCustom{

	
	@Query(value = "DELETE "
				 + "FROM arquivo_pronto ap "
				 + "WHERE ap.cnpj_empresa = :cnpjEmpresa "
				 + "AND   ap.cnpj_contabilidade = :cnpjContabilidade "
				 + "AND   ap.lote = :lote")
	void deletaPorLoteECnpjs(@Param("lote") String lote, @Param("cnpjEmpresa") String cnpjEmpresa, @Param("cnpjContabilidade") String cnpjContabilidade);
}
