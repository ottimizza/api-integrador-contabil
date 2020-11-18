package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;

@Repository
public interface GrupoRegraRepository extends JpaRepository<GrupoRegra, BigInteger>, GrupoRegraRepositoryCustom { // @formatter:off

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
    @Query(value = " update grupo_regras           " + 
           " set posicao = posicao - 1             " + 
           " where cnpj_empresa = :cnpjEmpresa     " + 
           " and tipo_lancamento = :tipoLancamento " + 
           " and posicao > :posicaoAnterior        " +
           " and posicao <= :posicaoAtual          ", nativeQuery = true)
    void decrementaPosicaoPorIntervalo(@Param("cnpjEmpresa") String cnpjEmpresa, 
                                       @Param("tipoLancamento") Short tipoLancamento,
                                       @Param("posicaoAnterior") Integer posicaoAnterior, 
                                       @Param("posicaoAtual") Integer posicaoAtual);
    
    @Modifying
    @Transactional
    @Query(value = " update grupo_regras            " + 
           " set posicao = posicao + 1              " + 
           " where cnpj_empresa = :cnpjEmpresa      " + 
           " and tipo_lancamento = :tipoLancamento  " + 
           " and posicao >= :posicaoAtual           " +
           " and posicao < :posicaoAnterior         ", nativeQuery = true)
    void incrementaPosicaoPorIntervalo(@Param("cnpjEmpresa") String cnpjEmpresa, 
                                       @Param("tipoLancamento") Short tipoLancamento,
                                       @Param("posicaoAnterior") Integer posicaoAnterior, 
                                       @Param("posicaoAtual") Integer posicaoAtual);


    @Modifying
    @Transactional
    @Query(value = " update grupo_regras set posicao = :posicao where id = :grupoRegraId ", nativeQuery = true)
    void atualizaPosicaoPorId(@Param("grupoRegraId") BigInteger grupoRegraId, 
                              @Param("posicao") Integer posicao);
    
    
    @Modifying
    @Transactional
    @Query(value = " select gr.id 							 " + 
    			   "from grupo_regras gr 					 " + 
    			   "where gr.cnpj_empresa = :cnpjEmpresa     " + 
    			   "and gr.tipo_lancamento = :tipoLancamento " +
                   "and gr.ativo = true                      " +
    			   "order by gr.posicao asc 				 ", nativeQuery = true)
    List<BigInteger>  findId(@Param("cnpjEmpresa")String cnpjEmpresa,
    						 @Param("tipoLancamento")Short tipoLancamento);
    
    @Modifying
    @Transactional
    @Query(value = "with grupo_regras_cte as (											     " + 
    		"	select 																		 " + 
    		"		id,     																 " + 
    		"		posicao, 																 " + 
    		"		(row_number() over (order by contagem_regras asc, posicao asc)) -1 as rn " + 
    		" from grupo_regras  							 				    			 " + 
    		" where cnpj_empresa     = :cnpjEmpresa 			 							 " + 
    		" and cnpj_contabilidade = :cnpjContabilidade 				 	    			 " + 
    		" and tipo_lancamento    = :tipoLancamento)				    				 	 " +  
    		" update grupo_regras set posicao = grupo_regras_cte.rn from grupo_regras_cte where grupo_regras.id = grupo_regras_cte.id",nativeQuery = true)
    void ajustePosicao(@Param("cnpjEmpresa")String cnpjEmpresa,
    				   @Param("cnpjContabilidade") String cnpjContabilidade,
    				   @Param("tipoLancamento") Short tipoLancamento);
    @Modifying
    @Transactional
    @Query(value = "UPDATE grupo_regras SET ativo = false, usuario = :usuario WHERE id = :grupoRegraId", nativeQuery = true)
    void inativarGrupoRegra(@Param("grupoRegraId") BigInteger grupoRegraId, @Param("usuario") String usuario);
}