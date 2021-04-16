package br.com.ottimizza.integradorcloud.repositories.banco;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.banco.Banco;
import feign.Param;



@Repository
public interface BancoRepository extends JpaRepository<Banco, BigInteger>, BancoRepositoryCustom{


    @Query(value = "SELECT b.* FROM bancos b WHERE b.descricao LIKE(%:nome) AND cnpj_empresa = :cnpjEmpresa ", nativeQuery = true)
    Banco findByNomeAndCnpjEmpresa(@Param("nome") String nome, @Param("cnpjEmpresa") String cnpjEmpresa);

    @Query(value = "SELECT b.* FROM bancos b WHERE b.cnpj_empresa = :cnpj AND b.fk_banco_padrao_id = :bancoId", nativeQuery = true)
    Banco findByCnpjAndBancoPadraoId(@Param("cnpj") String cnpj, @Param("bancoId") BigInteger bancoId);
    
}