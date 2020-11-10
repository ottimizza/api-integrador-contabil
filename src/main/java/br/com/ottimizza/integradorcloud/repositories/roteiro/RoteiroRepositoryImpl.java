package br.com.ottimizza.integradorcloud.repositories.roteiro;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;

public class RoteiroRepositoryImpl implements RoteiroRepositoryCustom{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Page<Roteiro> buscaRoteiros(RoteiroDTO filtro, PageCriteria criteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT r.* FROM roteiros r INNER JOIN empresas e ON (r.fk_empresa_id = e.id) WHERE r.fk_contabilidade_id = :contabilidadeId ");
		if(filtro.getNomeCompleto() != null && !filtro.getNomeCompleto().equals(""))
			sb.append("AND e.nome_completo ILIKE unaccent(:nomeCompleto) ");
		
		if(filtro.getNome() != null && !filtro.getNome().equals(""))
			sb.append("AND r.nome ILIKE unaccent(:nome) ");
		
		if(filtro.getStatus() !=null)
			sb.append("AND r.status = :status ");
		
		if(filtro.getTipoRoteiro() != null && !filtro.getTipoRoteiro().equals(""))
			sb.append("AND r.tipo_roteiro = :tipoRoteiro ");
		
		if(filtro.getId() != null)
			sb.append("AND r.id = :roteiroId ");

		if(filtro.getCnpjEmpresa() != null && !filtro.getCnpjEmpresa().equals(""))
			sb.append("AND r.cnpj_empresa = :cnpjEmpresa ");
		
		sb.append("ORDER BY e.razao_social ASC");
		
		// ---------------------------------------------------------------------------- //
		
		Query query = em.createNativeQuery(sb.toString(), Roteiro.class);
		query.setParameter("contabilidadeId", filtro.getContabilidadeId());
		
		if(filtro.getNomeCompleto() != null && !filtro.getNomeCompleto().equals(""))
			query.setParameter("nomeCompleto", "%"+filtro.getNomeCompleto()+"%");
		
		if(filtro.getNome() != null && !filtro.getNome().equals(""))
			query.setParameter("nome", "%"+filtro.getNome()+"%");
		
		if(filtro.getStatus() !=null)
			query.setParameter("status", filtro.getStatus());
		
		if(filtro.getTipoRoteiro() != null && !filtro.getTipoRoteiro().equals(""))
			query.setParameter("tipoRoteiro", filtro.getTipoRoteiro());
		
		if(filtro.getId() != null)
			query.setParameter("roteiroId", filtro.getId());

		if(filtro.getCnpjEmpresa() != null && !filtro.getCnpjEmpresa().equals(""))
			query.setParameter("cnpjEmpresa", filtro.getCnpjEmpresa());
		
		query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());
			
		return new PageImpl<Roteiro>(query.getResultList(), PageCriteria.getPageRequest(criteria), query.getResultList().size());
	}

}
