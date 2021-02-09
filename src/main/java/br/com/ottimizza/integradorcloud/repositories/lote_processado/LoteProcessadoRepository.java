package br.com.ottimizza.integradorcloud.repositories.lote_processado;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.LoteProcessado;

@Repository
public interface LoteProcessadoRepository extends JpaRepository<LoteProcessado, BigInteger>, LoteProcessadoRepositoryCustom  {

}
