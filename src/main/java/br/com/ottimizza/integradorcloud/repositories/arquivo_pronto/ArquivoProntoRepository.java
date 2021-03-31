package br.com.ottimizza.integradorcloud.repositories.arquivo_pronto;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;

@Repository
public interface ArquivoProntoRepository extends JpaRepository<ArquivoPronto, BigInteger>, ArquivoProntoRepositoryCustom{

}
