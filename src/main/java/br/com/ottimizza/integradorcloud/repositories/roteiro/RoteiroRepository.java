package br.com.ottimizza.integradorcloud.repositories.roteiro;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;

@Repository
public interface RoteiroRepository extends JpaRepository<Roteiro, BigInteger>, RoteiroRepositoryCustom{

	@Query(value = "SELECT COUNT(*) FROM roteiros r WHERE r.nome = :nome AND r.fk_empresa_id = :empresaId AND r.tipo_roteiro = :tipoRoteiro", nativeQuery = true)
	Integer buscaPorNomeEmpresaIdTipo(@Param("nome") String nome, @Param("empresaId") BigInteger empresaId, @Param("tipoRoteiro") String tipoRoteiro);
}
