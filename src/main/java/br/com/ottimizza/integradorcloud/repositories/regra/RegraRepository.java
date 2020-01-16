package br.com.ottimizza.integradorcloud.repositories.regra;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.Regra;

@Repository
public interface RegraRepository extends PagingAndSortingRepository<Regra, BigInteger> {

    @Modifying
    @Transactional
    @Query("delete from Regra gr where gr.grupoRegra.arquivo.cnpjEmpresa = :cnpjEmpresa")
    Integer apagarTodosPorCnpjEmpresa(@Param("cnpjEmpresa") String cnpjEmpresa);

}