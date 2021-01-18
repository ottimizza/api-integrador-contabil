package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.ottimizza.integradorcloud.domain.commands.lancamento.TotalLanvamentosArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.models.KPILancamento;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

@Repository // @formatter:off
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
    
    @Query(value = "select new br.com.ottimizza.integradorcloud.domain.commands.lancamento.TotalLanvamentosArquivoRequest(count(*), l.nomeArquivo) "
    		+ "     from Lancamento l "
    		+ "     where l.cnpjEmpresa = :cnpjEmpresa       and "
    		+ "     l.tipoMovimento = :tipoMovimento         and "
    		+ "     l.cnpjContabilidade = :cnpjContabilidade and"
    		+ "     l.ativo = true group by l.nomeArquivo       ")
    List<TotalLanvamentosArquivoRequest> lancamentosPorArquivo(@Param("cnpjEmpresa") String cnpjEmpresa,
    														   @Param("cnpjContabilidade") String cnpjContabilidade,
    														   @Param("tipoMovimento") String tipoMovimento);
    
    @Modifying
    @Transactional
    @Query("delete from Lancamento l where l.cnpjEmpresa = :cnpjEmpresa")
    Integer apagarTodosPorCnpjEmpresa(@Param("cnpjEmpresa") String cnpjEmpresa);
    
    @Modifying
    @Transactional
    @Query("update Lancamento l set l.contaSugerida = :contaSugerida where l.cnpjEmpresa = :cnpjEmpresa and l.descricao = :descricao")
    Integer atualizarContaSugeridaPorDescricao(@Param("descricao") String descricao, 
                                               @Param("contaSugerida") String contaSugerida, 
                                               @Param("cnpjEmpresa") String cnpjEmpresa);


    @Modifying
    @Transactional
    @Query(" update Lancamento l                     " + 
           " set l.contaMovimento = :contaMovimento, " + 
           " l.tipoConta      = 1                    " + 
           " where l.cnpjEmpresa = :cnpjEmpresa      " + 
           " and l.tipoLancamento = :tipoLancamento  " + 
           " and l.descricao = :descricao            ")
    Integer atualizarContaMovimentoPorDescricaoETipoLancamento(@Param("descricao") String descricao, 
                                                               @Param("tipoLancamento") Short tipoLancamento,
                                                               @Param("contaMovimento") String contaMovimento, 
                                                               @Param("cnpjEmpresa") String cnpjEmpresa);
    
    @Modifying
    @Transactional
    @Query(" update Lancamento l                    " + 
            " set l.contaSugerida = :contaSugerida " + 
            " where l.cnpjEmpresa = :cnpjEmpresa     " + 
            " and l.tipoLancamento = :tipoLancamento " + 
            " and l.descricao = :descricao           ")
    Integer atualizarContaSugeridaPorDescricaoETipoLancamento(@Param("descricao") String descricao, 
                                                                @Param("tipoLancamento") Short tipoLancamento,
                                                                @Param("contaSugerida") String contaSugerida, 
                                                                @Param("cnpjEmpresa") String cnpjEmpresa);
    
    @Modifying
    @Transactional
    @Query(value = " UPDATE lancamentos 				"
    			 + " SET ativo = false     				"
			 	 + " WHERE fk_arquivos_id = :id_arquivo ", nativeQuery = true)
    Integer atualizaStatus(@Param("id_arquivo") BigInteger id_arquivo);
    
    @Modifying
    @Transactional
    @Query(value = " DELETE 							"
    			 + " FROM lancamentos 					"
    			 + " WHERE ativo = false				", nativeQuery = true)
    Integer deleteLancamentosInativos();

    @Modifying
    @Transactional
    @Query(value = " UPDATE lancamentos l            "
                 + " SET conta_movimento = null,     "
                 + " tipo_conta = 0,                 "
                 + " fk_regras_id = null             "
                 + " WHERE l.fk_regras_id = :regraId ", nativeQuery = true)
    void restaurarPorRegraId(@Param("regraId") BigInteger regraId);
    
    @Query(value = "SELECT COUNT(*) FROM Lancamento l	 "
    			 + "WHERE l.cnpjEmpresa = :cnpjEmpresa   "
    			 + "AND l.tipoMovimento = :tipoMovimento "
    			 + "AND l.tipoConta = 0 				 "
    			 + "AND l.ativo = true  				 ")
    Long contarLancamentosRestantesEmpresa(@Param("cnpjEmpresa") String cnpjEmpresa, 
    								   	   	  @Param("tipoMovimento") String tipoMovimento);

    @Query(value = "SELECT COUNT(*) FROM Lancamento l	 "
			 	 + "WHERE l.cnpjEmpresa = :cnpjEmpresa   "
			 	 + "AND l.tipoMovimento = :tipoMovimento "
			 	 + "AND l.ativo = true  				 ")
    Long contarTotalLancamentosEmpresa(@Param("cnpjEmpresa") String cnpjEmpresa, 
    									  @Param("tipoMovimento") String tipoMovimento);
    
}
