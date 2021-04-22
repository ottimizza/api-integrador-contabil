package br.com.ottimizza.integradorcloud.repositories.layout_padrao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LayoutPadraoDTO;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;

public class LayoutPadraoRepositoryImpl implements LayoutPadraoRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    public Page<LayoutPadrao> buscaComFiltro(LayoutPadraoDTO filtro, PageCriteria criteria) {
        StringBuilder sql = new StringBuilder();
        boolean where = false;
        sql.append("SELECT lp.* ");
        sql.append("FROM layouts_padroes lp ");

        if(filtro.getDescricaoDocumento() != null && !filtro.getDescricaoDocumento().equals("")){
            System.out.println("entrou");
            sql.append("WHERE lp.descricao_documento ILIKE :descricaoDocumento ");
            where =  true;
        }
        if(filtro.getTipoArquivo() != null && !filtro.getTipoArquivo().equals("")) {
            if(!where){
                sql.append("WHERE lp.tipo_arquivo = :tipoArquivo ");
                where =  true;
            }
            else {
                sql.append("AND lp.tipo_arquivo = :tipoArquivo ");
            }
        }
        if(filtro.getTipoIntegracao() != null) {
            if(!where){
                sql.append("WHERE lp.tipo_integracao = :tipoIntegracao ");
                where =  true;
            }
            else {
                sql.append("AND lp.tipo_integracao = :tipoIntegracao ");
            }
        }
        if(filtro.getPagamentos() != null) {
            if(!where){
                sql.append("WHERE lp.pagamentos = :pagamentos ");
                where =  true;
            }
            else {
                sql.append("AND lp.pagamentos = :pagamentos ");
            }
        }
        if(filtro.getRecebimentos() != null) {
            if(!where){
                sql.append("WHERE lp.recebimentos = :recebimentos ");
                where =  true;
            }
            else {
                sql.append("AND lp.recebimentos = :recebimentos ");
            }
        }
        sql.append("ORDER BY lp.descricao_documento ASC ");

        Query query = em.createNativeQuery(sql.toString(), LayoutPadrao.class);

        if(filtro.getDescricaoDocumento() != null && !filtro.getDescricaoDocumento().equals("")){
            query.setParameter("descricaoDocumento", filtro.getDescricaoDocumento());
        }

        if(filtro.getTipoArquivo() != null && !filtro.getTipoArquivo().equals("")) {
            query.setParameter("tipoArquivo", filtro.getTipoArquivo());
        }

        if(filtro.getTipoIntegracao() != null) {
            query.setParameter("tipoIntegracao", filtro.getTipoIntegracao());    
        }

        if(filtro.getPagamentos() != null) {
            query.setParameter("pagamentos", filtro.getPagamentos());
        }

        if(filtro.getRecebimentos() != null) {
            query.setParameter("recebimentos", filtro.getRecebimentos());
        }

        long totalElements = query.getResultList().size();
		query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());

        return new PageImpl<LayoutPadrao>(query.getResultList(), PageCriteria.getPageRequest(criteria), totalElements);
    }
    
}
