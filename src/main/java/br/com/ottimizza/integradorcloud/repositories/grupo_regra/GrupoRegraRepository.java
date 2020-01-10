package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;

@Repository
public interface GrupoRegraRepository
        extends PagingAndSortingRepository<GrupoRegra, BigInteger>, GrupoRegraRepositoryCustom {
}