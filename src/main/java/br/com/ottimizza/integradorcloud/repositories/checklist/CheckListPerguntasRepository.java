package br.com.ottimizza.integradorcloud.repositories.checklist;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListObservacoes;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListPerguntas;
import feign.Param;

@Repository
public interface CheckListPerguntasRepository  extends JpaRepository<CheckListPerguntas, BigInteger>{

	@Query(value = "SELECT cp.* FROM checklist_perguntas cp WHERE cp.tipo LIKE %:tipo% ORDER BY cp.descricao ASC", nativeQuery = true)
	CheckListPerguntas[] buscaPorTipo(@Param("tipo") String tipo);
	
	@Query(value = "SELECT new br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListObservacoes(co.id, co.descricao, co.posicao, co.importante) FROM CheckListObservacoes co ORDER BY co.posicao")
	List<CheckListObservacoes> buscaObservacoes();
	
	@Query(value = "SELECT cp.id FROM checklist_perguntas cp WHERE cp.descricao like('%analista%')", nativeQuery = true)
	List<BigInteger> getIdsPerguntasContanto();
	
	@Query(value = "SELECT e.nome_resumido FROM roteiros r INNER JOIN empresas e on (e.id = r.fk_empresa_id ) WHERE r.id = :roteiroId", nativeQuery = true)
	String getNomeEmpresaPorRoteiroId(@Param("roteiroId") BigInteger roteiroId);

	@Query(value = "SELECT cp.descricao FROM checklist_perguntas cp WHERE cp.id = :perguntaId")
	String getDescricaoPorId(@Param("perguntaId") BigInteger perguntaId);
	
	/*@Query(value = "SELECT new br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListObservacoes(co.id, co.descricao, co.importante) FROM CheckListObservacoes co")
	CheckListObservacoes[] buscaObservacoes();*/
	
	
}
