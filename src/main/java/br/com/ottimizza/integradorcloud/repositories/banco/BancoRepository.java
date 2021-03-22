package br.com.ottimizza.integradorcloud.repositories.banco;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.banco.Banco;



@Repository
public interface BancoRepository extends JpaRepository<Banco, BigInteger>, BancoRepositoryCustom{

}