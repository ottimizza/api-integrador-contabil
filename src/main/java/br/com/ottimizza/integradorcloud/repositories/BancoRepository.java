package br.com.ottimizza.integradorcloud.repositories;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ottimizza.integradorcloud.domain.models.Banco;

public interface BancoRepository extends JpaRepository<Banco, BigInteger>{

}
