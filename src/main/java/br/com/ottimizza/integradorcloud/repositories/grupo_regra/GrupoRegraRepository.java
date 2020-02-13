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
    
    @Query(value = "select max(gr.id) from grupo_regras      "
                +  "where gr.cnpj_empresa = :cnpjEmpresa     "
                +  "and gr.tipo_lancamento = :tipoLancamento ", nativeQuery = true)
    Integer buscarUltimaPosicaoPorEmpresaETipoLancamento(@Param("cnpjEmpresa") String cnpjEmpresa, 
                                                         @Param("tipoLancamento") Short tipoLancamento);

}