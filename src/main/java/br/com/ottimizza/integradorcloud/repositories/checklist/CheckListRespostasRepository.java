package br.com.ottimizza.integradorcloud.repositories.checklist;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListRespostas;

@Repository
public interface CheckListRespostasRepository extends JpaRepository<CheckListRespostas, BigInteger>{

}
