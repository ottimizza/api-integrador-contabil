package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;

@Repository
public interface GrupoRegraRepository extends JpaRepository<GrupoRegra, BigInteger> { // @formatter:off

    @Modifying
    @Transactional
    @Query("delete from GrupoRegra gr where gr.cnpjEmpresa = :cnpjEmpresa")
    Integer apagarTodosPorCnpjEmpresa(@Param("cnpjEmpresa") String cnpjEmpresa);
    
    @Query(value = " select max(gr.posicao) from grupo_regras gr "
                +  " where gr.cnpj_empresa = :cnpjEmpresa        "
                +  " and gr.tipo_lancamento = :tipoLancamento    ", nativeQuery = true)
    Integer buscarUltimaPosicaoPorEmpresaETipoLancamento(@Param("cnpjEmpresa") String cnpjEmpresa, 
                                                         @Param("tipoLancamento") Short tipoLancamento);

    @Modifying
    @Transactional
    @Query(" update GrupoRegra r                     " + 
           " set r.posicao = r.posicao - 1           " + 
           " where r.cnpj_empresa = :cnpjEmpresa     " + 
           " and r.tipo_lancamento = :tipoLancamento " + 
           " and r.posicao > :posicaoAnterior        " +
           " and r.posicao <= :posicaoAtual          ")
    void decrementaPosicaoPorIntervalo(@Param("cnpjEmpresa") String cnpjEmpresa, 
                                       @Param("tipoLancamento") Short tipoLancamento,
                                       @Param("posicaoAnterior") Integer posicaoAnterior, 
                                       @Param("posicaoAtual") Integer posicaoAtual);
    
    @Modifying
    @Transactional
    @Query(" update GrupoRegra r                     " + 
           " set r.posicao = r.posicao + 1           " + 
           " where r.cnpj_empresa = :cnpjEmpresa     " + 
           " and r.tipo_lancamento = :tipoLancamento " + 
           " and r.posicao >= :posicaoAtual          " +
           " and r.posicao < :posicaoAnterior        ")
    void incrementaPosicaoPorIntervalo(@Param("cnpjEmpresa") String cnpjEmpresa, 
                                       @Param("tipoLancamento") Short tipoLancamento,
                                       @Param("posicaoAnterior") Integer posicaoAnterior, 
                                       @Param("posicaoAtual") Integer posicaoAtual);


    @Modifying
    @Transactional
    @Query(" update GrupoRegra r set r.posicao = :posicao where r.id >= :grupoRegraId ")
    void atualizaPosicaoPorId(@Param("grupoRegraId") BigInteger grupoRegraId, 
                              @Param("posicao") Integer posicao);

}