package br.com.ottimizza.integradorcloud.repositories.saldo_bancos;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.ViewSaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;
import br.com.ottimizza.integradorcloud.domain.models.banco.ViewSaldoBancos;

@Repository
public interface SaldoBancosRepository extends JpaRepository<SaldoBancos, BigInteger>, SaldoBancosRepositoryCustom{
    

    @Query(value = "SELECT sb.* FROM saldo_bancos sb WHERE sb.fk_banco_id = :bancoId AND sb.data = :data", nativeQuery = true)
    SaldoBancos buscaPorBancoData(@Param("bancoId") BigInteger bancoId, @Param("data") LocalDate data); 


    @Query(value = "SELECT SUM(lc.valor_final) FROM livros_caixas lc WHERE lc.cnpj_empresa = :cnpjEmpresa AND lc.data_movimento >= :data AND lc.fk_banco_id = :bancoId AND lc.status = 1", nativeQuery = true)
    Double contarValoresBanco(@Param("cnpjEmpresa") String cnpjEmpresa, @Param("data") LocalDate data, @Param("bancoId") BigInteger bancoId);

    @Query(value = "SELECT sb.* FROM saldo_bancos sb WHERE sb.fk_banco_id = :bancoId AND sb.data > :data LIMIT 1", nativeQuery = true)
    SaldoBancos buscaPorBancoDataMaior(@Param("bancoId") BigInteger bancoId, @Param("data") LocalDate data);

    /*@Query(value ="SELECT new br.com.ottimizza.integradorcloud.domain.dtos.banco.ViewSaldoBancosDTO(ViewSaldoBancos.id, ViewSaldoBancos.banco, ViewSaldoBancos.data, ViewSaldoBancos.saldo, ViewSaldoBancos.cnpjEmpresa, ()) "
                 +"FROM ViewSaldoBancos "
                 +"WHERE ViewSaldoBancos.cnpjEmpresa = :cnpjEmpresa ")
    List<ViewSaldoBancosDTO> buscaUltimoSaldoPorBanco(@Param("cnpjEmpresa") String cnpjEmpresa);*/ 

    
    

}


