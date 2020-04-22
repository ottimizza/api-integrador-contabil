package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.Historico;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, BigInteger> {

    @Query("select h from Historico h where h.cnpjEmpresa = :cnpjEmpresa and h.contaMovimento = :contaMovimento and h.tipoLancamento = :tipoLancamento ")
    Historico buscarPorContaMovimento(@Param("contaMovimento") String contaMovimento,
    								  @Param("cnpjEmpresa")    String cnpjEmpresa, 
    								  @Param("tipoLancamento") Short tipoLancamento);

}