package br.com.ottimizza.integradorcloud.repositories.livro_caixa;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public class LivroCaixaRepositoryImpl implements LivroCaixaRepositoryCustom{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria) {
		String mesAno = "";
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * 		 	");
		sql.append("FROM livro_caixa lc ");
		sql.append("WHERE lc.cnpj_contabilidade = :cnpjContabilidade ");
		sql.append("AND lc.cnpj_empresa = :cnpjEmpresa ");
		
		if(filtro.getId() != null)
			sql.append("AND lc.id = :id ");
		if(filtro.getComplemento() != null && !filtro.getComplemento().equals(""))
			sql.append("AND lc.complemento ILIKE(:complemento) ");
		if(filtro.getDescricao() != null && !filtro.getDescricao().equals(""))
			sql.append("AND lc.descricao ILIKE(:descricao) ");
		if(filtro.getBancoId() != null)
			sql.append("AND lc.fk_banco_id = :bancoId ");
		if(filtro.getCategoriaId() != null) 
			sql.append("AND lc.fk_categoria_id = :categoriaId ");
		if(filtro.getDataString() != null && !filtro.getDataString().equals("")) {
			mesAno = filtro.getDataString();
			sql.append("AND lc.data_movimento > :dataMovimento1 ");
			sql.append("AND lc.data_movimento < :dataMovimento2 ");
		}
		if(filtro.getStatus() != null)
			sql.append("AND lc.status = :status ");
		if(filtro.getTipoMovimento() != null && !filtro.getTipoMovimento().equals(""))
			sql.append("AND lc.tipo_movimento = :tipoMovimento ");
		
			
		Query query = em.createNativeQuery(sql.toString(), LivroCaixa.class);
		query.setParameter("cnpjContabilidade", filtro.getCnpjContabilidade());
		query.setParameter("cnpjEmpresa", filtro.getCnpjEmpresa());
		
		if(filtro.getId() != null)
			query.setParameter("id", filtro.getId());
		if(filtro.getComplemento() != null && !filtro.getComplemento().equals(""))
			query.setParameter("complemento", "%"+filtro.getComplemento()+"%");
		if(filtro.getDescricao() != null && !filtro.getDescricao().equals(""))
			query.setParameter("descricao", "%"+filtro.getDescricao()+"%");
		if(filtro.getBancoId() != null)
			query.setParameter("bancoId", filtro.getBancoId());
		if(filtro.getCategoriaId() != null) 
			query.setParameter("categoriaId", filtro.getCategoriaId());
		if(filtro.getDataString() != null && !filtro.getDataString().equals("")) {
			LocalDate data1 = LocalDate.of(Integer.parseInt(mesAno.substring(3)), Integer.parseInt(mesAno.substring(0,1)), 1);
			LocalDate data2 = LocalDate.of(Integer.parseInt(mesAno.substring(3)), Integer.parseInt(mesAno.substring(0,1)), 31);
			query.setParameter("dataMovimento1", data1);
			query.setParameter("dataMovimento2", data2);
		}
		if(filtro.getStatus() != null)
			query.setParameter("status", filtro.getStatus());
		if(filtro.getTipoMovimento() != null && !filtro.getTipoMovimento().equals(""))
			query.setParameter("tipoMovimento", filtro.getTipoMovimento());
		
		long totalElements = query.getResultList().size();
		query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());
			
		return new PageImpl<LivroCaixa>(query.getResultList(), PageCriteria.getPageRequest(criteria), totalElements);
	}

}
