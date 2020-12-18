package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
 

public class GrupoRegraRepositoryImpl implements GrupoRegraRepositoryCustom {

	@PersistenceContext
    private EntityManager em;
	
	@Override
	public GrupoRegra sugerirRegra(Short busca, BigInteger lancamentoId, String cnpjContabilidade) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT grupo_regras.* ");
		sb.append("FROM grupo_regras  	");
		sb.append("INNER JOIN lancamentos ON lancamentos.campos @>  grupo_regras.campos ");
		sb.append("WHERE lancamentos.id = :lancamentoId ");
		sb.append("AND grupo_regras.contagem_regras > 2 ");
		sb.append("AND grupo_regras.ativo = true ");
		sb.append("AND NOT EXISTS(SELECT 1 FROM regras r2 WHERE r2.fk_grupo_regras_id = grupo_regras.id AND r2.condicao = 2) ");
		//sb.append("AND grupo_regras.id not in(SELECT gr.id FROM grupo_regras_ignoradas gr WHERE gr.cnpj_contabilidade = :cnpjContabilidade)");
		if(busca == 1) {
			sb.append("AND grupo_regras.cnpj_contabilidade = :cnpjContabilidade ");
		}
		//sb.append("AND grupo_regras.id = 2014 ");
		sb.append("ORDER BY grupo_regras.contagem_regras DESC, ");
		sb.append("         grupo_regras.peso_regras DESC, ");
		sb.append("			grupo_regras.id DESC ");
		sb.append("LIMIT 1");
		
		Query query = em.createNativeQuery(sb.toString(), GrupoRegra.class);
		query.setParameter("lancamentoId", lancamentoId);
		
		if(busca == 1)
			query.setParameter("cnpjContabilidade", cnpjContabilidade);
		
		return (GrupoRegra) query.getSingleResult();
	}

	@Override
	public GrupoRegra buscarPorCamposContabilidade(String cnpjContabilidade, List<String> campos) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT grupo_regras.* 									");
		sb.append("FROM grupo_regras  										");
		sb.append("WHERE grupo_regras.campos @> :campos 					");
		sb.append("AND :campos @> grupo_regras.campos 						");
		sb.append("AND grupo_regras.cnpj_contabilidade = :cnpjContabilidade ");
		sb.append("AND grupo_regras.ativo = true 							");
		sb.append("ORDER BY grupo_regras.contagem_regras DESC, 				");
		sb.append("         grupo_regras.peso_regras DESC, 					");
		sb.append("			grupo_regras.id DESC							");
		sb.append("LIMIT 1");
		
		Query query = em.createNativeQuery(sb.toString(), GrupoRegra.class);
		query.setParameter("campos", campos);
		query.setParameter("cnpjContabilidade", cnpjContabilidade);
		
		return (GrupoRegra) query.getSingleResult();
	}
	
	


}
