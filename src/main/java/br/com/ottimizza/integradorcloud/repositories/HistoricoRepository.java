package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;
import java.util.List;

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

    @Query("SELECT h FROM Historico h WHERE h.cnpjContabilidade = :cnpjContabilidade and h.cnpjEmpresa = :cnpjEmpresa and h.tipoLancamento = :tipoLancamento")
    List<Historico> buscaHistoricosParaSalesForce(@Param("cnpjContabilidade") String cnpjContabilidade,
    											  @Param("cnpjEmpresa")       String cnpjEmpresa,
    											  @Param("tipoLancamento")    Short tipoLancamento);
}