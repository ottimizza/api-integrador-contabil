package br.com.ottimizza.integradorcloud.repositories.livro_caixa;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

@Repository
public interface LivroCaixaRepository extends JpaRepository<LivroCaixa, BigInteger>, LivroCaixaRepositoryCustom{

//	LivroCaixa findByCnpjContabilidadeAndCnpjEmpresaFirstByOrderByIdDesc(String cnpjContabilidade, String cnpjEmpresa);
//	LivroCaixa findByCnpjContabilidadeAndCnpjEmpresa(String cnpjContabilidade, String cnpjEmpresa);

}
