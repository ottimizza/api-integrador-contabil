package br.com.ottimizza.integradorcloud.repositories.banco;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancoDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.Banco;

public class BancoRepositoryImpl implements BancoRepositoryCustom {


    @PersistenceContext
    EntityManager em;

    @Override
    public Page<Banco> buscaComFiltro(BancoDTO filtro, PageCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT b.* ");
        sql.append("FROM bancos b ");
        sql.append("LEFT JOIN bancos_padroes bp ");
        sql.append("ON bp.id = b.fk_banco_padrao_id ");
        sql.append("WHERE b.cnpj_contabilidade = :cnpjContabilidade ");
        if(filtro.getDescricao() != null && !filtro.getDescricao().equals(""))
            sql.append("AND b.descricao LIKE(:descricao)");
        if(filtro.getCodigoBanco() != null)
            sql.append("AND b.codigo_banco LIKE(:codBanco)");
        
        Query query = em.createNativeQuery(sql.toString());
        query.setParameter("cnpjContabilidade", filtro.getCnpjContabilidade());

        if(filtro.getDescricao() != null && !filtro.getDescricao().equals(""))
            query.setParameter("descricao","%"+filtro.getDescricao()+"%");
        if(filtro.getCodigoBanco() != null)
            query.setParameter("codBanco","%"+filtro.getCodigoBanco()+"%");
        
        long totalElements = query.getResultList().size();
        query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());
        
        return new PageImpl<Banco>(query.getResultList(), PageCriteria.getPageRequest(criteria), totalElements);
    }
    
}