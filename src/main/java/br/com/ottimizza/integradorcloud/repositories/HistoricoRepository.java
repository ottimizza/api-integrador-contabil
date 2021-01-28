package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.Historico;

import javax.transaction.Transactional;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, BigInteger> {

    @Query("select h from Historico h where h.cnpjEmpresa = :cnpjEmpresa and h.contaMovimento = :contaMovimento and h.tipoLancamento = :tipoLancamento and h.ativo = true")
    Historico buscarPorContaMovimento(@Param("contaMovimento") String contaMovimento,
    								  @Param("cnpjEmpresa")    String cnpjEmpresa, 
    								  @Param("tipoLancamento") Short tipoLancamento);

    @Query("SELECT h FROM Historico h WHERE h.cnpjContabilidade = :cnpjContabilidade and h.cnpjEmpresa = :cnpjEmpresa and h.tipoLancamento = :tipoLancamento and h.ativo = true")
    List<Historico> buscaHistoricosParaSalesForce(@Param("cnpjContabilidade") String cnpjContabilidade,
    											  @Param("cnpjEmpresa")       String cnpjEmpresa,
    											  @Param("tipoLancamento")    Short tipoLancamento);
    
    @Query(value = "SELECT h.* "
    		     + "FROM historicos h "
    		     + "WHERE h.cnpj_contabilidae = :cnpjContabilidade "
    		     + "AND h.conta_movimento = :contaMovimento "
    		     + "LIMIT 1", nativeQuery = true)
    Historico buscaPorContaMovimentoContabilidade(@Param("contaMovimento")    String contaMovimento,
			  									  @Param("cnpjContabilidade") String cnpjContabilidade);

    @Modifying
    @Transactional
    @Query(value = "UPDATE historicos SET ativo = false, usuario = :usuario WHERE id = :historicoId", nativeQuery = true)
    void inativarHistorico(@Param("historicoId") BigInteger historicoId, @Param("usuario") String usuario);
}