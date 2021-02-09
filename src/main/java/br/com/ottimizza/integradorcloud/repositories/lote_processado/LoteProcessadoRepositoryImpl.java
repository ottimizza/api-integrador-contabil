package br.com.ottimizza.integradorcloud.repositories.lote_processado;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.lote_processado.LoteProcessadoDTO;
import br.com.ottimizza.integradorcloud.domain.models.LoteProcessado;

public class LoteProcessadoRepositoryImpl implements LoteProcessadoRepositoryCustom{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Page<LoteProcessado> buscaComFiltro(LoteProcessadoDTO filtro, PageCriteria criteria) {
		StringBuilder sql =  new StringBuilder();
		
		sql.append("SELECT * ");
		sql.append("FROM lotes_processados lp ");
		sql.append("WHERE lp.cnpj_empresa = :cnpjEmpresa ");
		sql.append("AND lp.cnpj_contabilidade = :cnpjContabilidade ");
		
		if(filtro.getId() != null)
			sql.append("AND lp.id = :id ");
		if(filtro.getLote() != null && !filtro.getLote().equals(""))
			sql.append("AND lp.lote ILIKE(:lote) ");
		if(filtro.getTipoRegistro() != null && !filtro.getTipoRegistro().equals(""))
			sql.append("AND lp.tipo_registro = :tipoRegistro ");
		if(filtro.getAnoMes() != null)
			sql.append("AND lp.ano_mes = :anoMes ");
		if(filtro.getQuantidadeLancamentos() != null )
			sql.append("AND lp.quantidade_lancamentos >= :quantidadeLancamentos ");
		
		sql.append("ORDER BY lp.data_criacao DESC ");
		
		Query query = em.createNativeQuery(sql.toString(), LoteProcessado.class);
		
		query.setParameter("cnpjEmpresa", filtro.getCnpjEmpresa());
		query.setParameter("cnpjContabilidade", filtro.getCnpjContabilidade());
		
		if(filtro.getId() != null)
			query.setParameter("id", filtro.getId());
		if(filtro.getLote() != null && !filtro.getLote().equals(""))
			query.setParameter("lote", "%"+filtro.getLote()+"%");
		if(filtro.getTipoRegistro() != null && !filtro.getTipoRegistro().equals(""))
			query.setParameter("tipoRegistro", filtro.getTipoRegistro());
		if(filtro.getAnoMes() != null)
			query.setParameter("anoMes", filtro.getAnoMes());
		if(filtro.getQuantidadeLancamentos() != null )
			query.setParameter("quantidadeLancamentos", filtro.getQuantidadeLancamentos());
		
		long totalElements = query.getResultList().size();
		query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());
		
		return new PageImpl<>(query.getResultList(), PageCriteria.getPageRequest(criteria), totalElements);
	}

}
