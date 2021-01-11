package br.com.ottimizza.integradorcloud.repositories.livro_caixa;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public interface LivroCaixaRepository  extends JpaRepository<LivroCaixa, BigInteger>, LivroCaixaRepositoryCustom{

}
