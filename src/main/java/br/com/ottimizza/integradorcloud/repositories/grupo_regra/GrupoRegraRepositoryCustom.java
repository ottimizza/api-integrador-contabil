package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.math.BigInteger;
import java.util.List;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;

public interface GrupoRegraRepositoryCustom {
	
	GrupoRegra sugerirRegra(Short busca, BigInteger lancamentoId, String cnpjContabilidade);

	GrupoRegra buscarPorCamposContabilidade(String cnpjContabilidade, BigInteger id);
	
}
