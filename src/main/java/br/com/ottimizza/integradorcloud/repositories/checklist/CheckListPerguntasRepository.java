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

	@Query(value = "SELECT cp.* FROM checklist_perguntas cp WHERE cp.tipo LIKE %:tipo% ORDER BY cp.id ASC", nativeQuery = true)
	CheckListPerguntas[] buscaPorTipo(@Param("tipo") String tipo);
	
	@Query(value = "SELECT new br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListObservacoes(co.id, co.descricao, co.importante) FROM CheckListObservacoes co")
	List<CheckListObservacoes> buscaObservacoes();
	
	
	/*@Query(value = "SELECT new br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListObservacoes(co.id, co.descricao, co.importante) FROM CheckListObservacoes co")
	CheckListObservacoes[] buscaObservacoes();*/
	
	
}
