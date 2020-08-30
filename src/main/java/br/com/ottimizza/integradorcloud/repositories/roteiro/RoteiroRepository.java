package br.com.ottimizza.integradorcloud.repositories.roteiro;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;

@Repository
public interface RoteiroRepository extends JpaRepository<Roteiro, BigInteger>{

}
