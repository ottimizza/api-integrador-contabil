package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.models.KPILancamento;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, BigInteger>, LancamentoRepositoryCustom {

    @Query(value = " with _lancamentos as (                                                                     "
            + "   select _l.id, _l.tipo_conta                                                                   "
            + "   from lancamentos _l                                                                           "
            + "   where _l.cnpj_empresa = :cnpjEmpresa                                                          "
            + " )                                                                                               "
            + " select                                                                                          "
            + "   (select count(_l0.id) from _lancamentos _l0) as todos,                                        "
            + "   (select count(_l0.id) from _lancamentos _l0 where _l0.tipo_conta = 1) as fornecedorCliente,   "
            + "   (select count(_l0.id) from _lancamentos _l0 where _l0.tipo_conta = 2) as outrasContas,        "
            + "   (select count(_l0.id) from _lancamentos _l0 where _l0.tipo_conta = 3) as ignorar,             "
            + "   (select count(_l0.id) from _lancamentos _l0 where _l0.tipo_conta = 3) as pular                "
            + " from _lancamentos l limit 1                                                                     ", nativeQuery = true)
    KPILancamento buscaStatusLancementosPorCNPJEmpresa(@Param("cnpjEmpresa") String cnpjEmpresa);

    @Modifying
    @Transactional
    @Query("delete from Lancamento l where l.cnpjEmpresa = :cnpjEmpresa")
    Integer apagarTodosPorCnpjEmpresa(@Param("cnpjEmpresa") String cnpjEmpresa);

}
