package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;
import feign.Param;

@Repository
public interface BancosPadroesRepository extends JpaRepository<BancosPadroes, BigInteger> {

    @Query(value = "SELECT bp.* FROM bancos_padroes bp where bp.codigo_banco = :codigo", nativeQuery = true)
    BancosPadroes findByCodigo(@Param("codigo") String codigo);

}