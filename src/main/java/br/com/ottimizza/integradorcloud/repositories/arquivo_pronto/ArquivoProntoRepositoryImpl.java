package br.com.ottimizza.integradorcloud.repositories.arquivo_pronto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.ArquivoProntoDTO;
import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;

public class ArquivoProntoRepositoryImpl implements ArquivoProntoRepositoryCustom{

	@PersistenceContext
	EntityManager em;

	@Override
	public Page<ArquivoPronto> buscaComFiltro(ArquivoProntoDTO filtro, PageCriteria criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM arquivo_pronto ap ");
		sql.append("WHERE ap.cnpj_contabilidade = :cnpjContabilidade ");
		sql.append("AND ap.cnpj_empresa = :cnpjEmpresa");
		if(filtro.getHistorico() != null && !filtro.getHistorico().equals(""))
			sql.append("AND ap.historico ILIKE('&:historico%') ");

		if(filtro.getCodigoHistorico() != null && !filtro.getCodigoHistorico().equals(""))
			sql.append("AND ap.codigo_historico ILIKE('%:codigoHistorico%') ");

		if(filtro.getContaCredito() != null && !filtro.getContaCredito().equals(""))
			sql.append("AND ap.conta_credito ILIKE('%:contaCredito%') ");

		if(filtro.getContaDebito() != null && !filtro.getContaDebito().equals(""))
			sql.append("AND ap.conta_debito ILIKE('%:contaDebito%') ");

		if(filtro.getTipoLancamento() != null && !filtro.getTipoLancamento().equals(""))
			sql.append("AND ap.tipo_lancamento ILIKE('%:tipoLancamento%') ");

		if(filtro.getTipoMovimento() != null && !filtro.getTipoMovimento().equals(""))
			sql.append("AND ap.tipo_movimento ILIKE('%:tipoMovimento%') ");

		if(filtro.getErpContabil() != null && !filtro.getErpContabil().equals(""))
			sql.append("AND ap.erp_contabil ILIKE('%:erpContabil%' ");

		if(filtro.getLote() != null && !filtro.getLote().equals(""))
			sql.append("AND ap.lote ILIKE('%:lote%') ");

		Query query = em.createNativeQuery(sql.toString(), ArquivoPronto.class);
		query.setParameter("cnpjContabilidade", filtro.getCnpjContabilidade());
		query.setParameter("cnpjEmpresa", filtro.getCnpjEmpresa());

		if(filtro.getHistorico() != null && !filtro.getHistorico().equals(""))
			query.setParameter("historico", filtro.getHistorico());

		if(filtro.getCodigoHistorico() != null && !filtro.getCodigoHistorico().equals(""))
			query.setParameter("codigoHistorico", filtro.getCodigoHistorico());

		if(filtro.getContaCredito() != null && !filtro.getContaCredito().equals(""))
			query.setParameter("contaCredito", filtro.getContaCredito());

		if(filtro.getContaDebito() != null && !filtro.getContaDebito().equals(""))
			query.setParameter("contaDebito", filtro.getContaDebito());

		if(filtro.getTipoLancamento() != null && !filtro.getTipoLancamento().equals(""))
			query.setParameter("tipoLancamento", filtro.getTipoLancamento());

		if(filtro.getTipoMovimento() != null && !filtro.getTipoMovimento().equals(""))
			query.setParameter("tipoMovimento", filtro.getTipoMovimento());

		if(filtro.getErpContabil() != null && !filtro.getErpContabil().equals(""))
			query.setParameter("erpContabil", filtro.getErpContabil());

		if(filtro.getLote() != null && !filtro.getLote().equals(""))
			query.setParameter("lote", filtro.getLote());

		long totalElements = query.getResultList().size();
		query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());

		return new PageImpl<>(query.getResultList(), PageCriteria.getPageRequest(criteria), totalElements);
	}

}
