package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;

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
		sb.append("AND NOT EXISTS(SELECT 1 FROM regras r2 WHERE r2.fk_grupo_regras_id = grupo_regras.id AND r2.condicao = 2) ");
		if(busca == 1) {
			sb.append("AND grupo_regras.cnpj_contabilidade = :cnpjContabilidade ");
		}
		//sb.append("AND grupo_regras.id = 2014 ");
		sb.append("ORDER BY grupo_regras.contagem_regras DESC ");
		sb.append("LIMIT 1");
		
		Query query = em.createNativeQuery(sb.toString(), GrupoRegra.class);
		query.setParameter("lancamentoId", lancamentoId);
		
		if(busca == 1)
			query.setParameter("cnpjContabilidade", cnpjContabilidade);
		
		return (GrupoRegra) query.getSingleResult();
	}
	

}
