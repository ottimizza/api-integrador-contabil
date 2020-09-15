package br.com.ottimizza.integradorcloud.repositories.checklist;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListRespostas;
import feign.Param;

@Repository
public interface CheckListRespostasRepository extends JpaRepository<CheckListRespostas, BigInteger>{

	@Query(value = "SELECT cr.* FROM checklist_respostas cr WHERE cr.fk_roteiro_id = :roteiroId AND cr.fk_pergunta_id = :perguntaId", nativeQuery = true)
	CheckListRespostas checkResposta(@Param("roteiroId") BigInteger roteiroId, @Param("perguntaId") BigInteger perguntaId);
}
