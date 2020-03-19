package br.com.ottimizza.integradorcloud.repositories.empresa;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, BigInteger> {

    @Query("select e from Empresa e where e.id = :id")
    Optional<Empresa> buscarPorId(@Param("id") BigInteger id);

    @Query("select e from Empresa e where e.organizationId = :organizationId")
    Optional<Empresa> buscarPorOrganizationId(@Param("organizationId") BigInteger organizationId);

    @Query("select e from Empresa e where e.cnpj = :cnpj")
    Optional<Empresa> buscarPorCNPJ(@Param("cnpj") String cnpj);

    @Query("select e from Empresa e where e.cnpj = :cnpj and e.accountingId = :accountingId")
    Optional<Empresa> buscaEmpresa(@Param("cnpj") String cnpj, @Param("accountingId") BigInteger accountingId);
    
}
