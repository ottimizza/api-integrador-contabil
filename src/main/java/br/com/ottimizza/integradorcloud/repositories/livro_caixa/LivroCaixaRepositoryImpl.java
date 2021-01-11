package br.com.ottimizza.integradorcloud.repositories.livro_caixa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.livro_caixa.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;

public class LivroCaixaRepositoryImpl implements LivroCaixaRepositoryCustom{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * 		 	");
		sql.append("FROM livro_caixa lc ");
		sql.append("WHERE lc.cnpj_contabilidade = :cnpjContabilidade ");
		sql.append("AND lc.cnpj_empresa = :cnpjEmpresa ");
		
		if(filtro.getId() != null)
			sql.append("AND lc.id = :id ");
		if(!filtro.getComplemento().equals(""))
			sql.append("AND lc.complemento ILIKE(%:complemento%) ");
		if(!filtro.getFornecerdor().equals(""))
			sql.append("AND lc.fornecedor ILIKE(%:fornecedor%) ");
		if(!filtro.getBanco().equals(""))
			sql.append("AND lc.banco ILIKE(%:banco%) ");
		if(filtro.getValorEntrada() != null)
			sql.append("AND lc.valor_entrada = :valorEntrada  ");
		if(filtro.getValorSaida() != null)
			sql.append("AND lc.valor_saida = :valorSaida  ");
		
			
		Query query = em.createNativeQuery(sql.toString(), LivroCaixa.class);
		query.setParameter("cnpjContabilidade", filtro.getCnpjContabilidade());
		query.setParameter("cnpjEmpresa", filtro.getCnpjEmpresa());
		
		if(filtro.getId() != null)
			query.setParameter("id", filtro.getId());
		if(!filtro.getComplemento().equals(""))
			query.setParameter("complemento", filtro.getComplemento());
		if(!filtro.getFornecerdor().equals(""))
			query.setParameter("fornecedor", filtro.getFornecerdor());
		if(!filtro.getBanco().equals(""))
			query.setParameter("banco", filtro.getBanco());
		if(filtro.getValorEntrada() != null)
			query.setParameter("valorEntrada", filtro.getValorEntrada());
		if(filtro.getValorSaida() != null)
			query.setParameter("valorSaida", filtro.getValorSaida());
		
		long totalElements = query.getResultList().size();
		query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());
			
		return new PageImpl<LivroCaixa>(query.getResultList(), PageCriteria.getPageRequest(criteria), totalElements);
	}

}
