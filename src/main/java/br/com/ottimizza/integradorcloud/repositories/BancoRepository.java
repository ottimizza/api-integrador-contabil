package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.Banco;

@Repository
public interface BancoRepository extends JpaRepository<Banco, BigInteger>{

}