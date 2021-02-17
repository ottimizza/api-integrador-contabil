package br.com.ottimizza.integradorcloud.repositories.categoria;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.Categoria;

@Repository
public interface CategoriaRepository  extends JpaRepository<Categoria, BigInteger> {

	
	@Query(value = "SELECT c.* 							 "
			     + "FROM categorias 			 		 "
			     + "WHERE c.descricao ILIKE(%:descricao%)", nativeQuery = true)
	List<Categoria> buscaPorDescricao(String descricao);
	
}
