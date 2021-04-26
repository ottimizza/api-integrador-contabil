package br.com.ottimizza.integradorcloud.repositories.layout_padrao;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;

@Repository
public interface LayoutPadraoRepository extends JpaRepository<LayoutPadrao, BigInteger>, LayoutPadraoRepositoryCustom {
    
}
