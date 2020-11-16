package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.math.BigInteger;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;

public interface GrupoRegraRepositoryCustom {
	
	GrupoRegra sugerirRegra(Short busca, BigInteger lancamentoId, String cnpjContabilidade);

}
