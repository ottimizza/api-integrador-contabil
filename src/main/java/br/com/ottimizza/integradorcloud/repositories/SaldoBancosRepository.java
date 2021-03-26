package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.ViewSaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;

@Repository
public interface SaldoBancosRepository extends JpaRepository<SaldoBancos, BigInteger>{
    

    @Query(value = "SELECT sb.* FROM saldo_bancos sb WHERE sb.fk_banco_id = :bancoId AND sb.data = :data", nativeQuery = true)
    SaldoBancos buscaPorBancoData(@Param("bancoId") BigInteger bancoId, @Param("data") LocalDate data); 


    @Query(value ="SELECT view_saldo_bancos.*,(SELECT SUM(lc.valor_final) "
                 +"FROM livros_caixas lc WHERE lc.cnpj_empresa = view_saldo_bancos.cnpj_empresa AND lc.data_movimento >= view_saldo_bancos.data AND lc.fk_banco_id = view_saldo_bancos.fk_banco_id AND lc.status = 1) valores "
                 +"FROM view_saldo_bancos "
                 +"WHERE view_saldo_bancos.cnpj_empresa = :cnpjEmpresa", nativeQuery = true)
    ViewSaldoBancosDTO buscaUltimoSaldoPorBanco(@Param("cnpjEmpresa") String cnpjEmpresa);

}
