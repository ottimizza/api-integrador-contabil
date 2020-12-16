package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;

public class LancamentoRepositoryImpl implements LancamentoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    
    private GrupoRegraRepository grupoRegraRepository;
    
    private RegraRepository regraRepository;

    long totalElements = 0;

    public Long contarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Lancamento> root = query.from(Lancamento.class);

        List<Predicate> predicates = predicates(regras, cb, root);
        predicates.add(0, cb.equal(root.get("cnpjEmpresa"), cnpjEmpresa));
        predicates.add(1, cb.equal(root.get("ativo"), true));

        query.select(cb.count(root));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        return em.createQuery(query).getSingleResult();
    }

    public Page<Lancamento> buscarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa, Pageable pageable, Principal principal) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lancamento> query = cb.createQuery(Lancamento.class);
        Root<Lancamento> root = query.from(Lancamento.class);

        List<Predicate> predicates = predicates(regras, cb, root);
        predicates.add(0, cb.equal(root.get("cnpjEmpresa"), cnpjEmpresa));
        predicates.add(1, cb.equal(root.get("ativo"), true));

        query.select(root);
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        TypedQuery<Lancamento> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        
    
        return new PageImpl<>(typedQuery.getResultList(), pageable, contarLancamentosPorRegra(regras, cnpjEmpresa));
    }

    @Modifying
    @Transactional
    public int atualizaLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa, String contaMovimento, BigInteger regraId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Lancamento> update = cb.createCriteriaUpdate(Lancamento.class);
        Root<Lancamento> root = update.from(Lancamento.class);

        List<Predicate> predicates = predicates(regras, cb, root);
        predicates.add(0, cb.equal(root.get("cnpjEmpresa"), cnpjEmpresa));

        update.set(root.get("contaMovimento"), contaMovimento);
        update.set(root.get("tipoConta"), Lancamento.TipoConta.OUTRAS_CONTAS);
        update.set(root.get("regraId"), regraId);
        update.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        // perform update and return affected rows.
        return this.em.createQuery(update).executeUpdate();
    }
    
    @Override
	public int atualizaLancamentosPorRegraNative(List<Regra> regras, String cnpjEmpresa, String cnpjContabilidade,  String contaMovimento, BigInteger regraId, Short sugerir, BigInteger regraSugerida) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE lancamentos l "); 
		sb.append("SET conta_movimento = :contaMovimento, "); 
		sb.append("	   tipo_conta = 2, "); 
        sb.append("	   fk_regras_id = :regraId ");
        sb.append("WHERE l.cnpj_empresa = :cnpjEmpresa ");
		for(Regra regra : regras) {
			switch (regra.getCondicao()) {
				case Regra.Condicao.CONTEM:
					sb.append("AND "+getCampo(regra.getCampo())+" ILIKE('%"+ regra.getValor().toUpperCase()+"%') ");
					break;
				case Regra.Condicao.NAO_CONTEM:
					sb.append("AND "+getCampo(regra.getCampo())+" NOT LIKE('%"+regra.getValor().toUpperCase()+"%') ");
					break;
				case Regra.Condicao.COMECAO_COM:
					sb.append("AND "+getCampo(regra.getCampo())+" LIKE('%"+regra.getValor().toUpperCase()+"') ");
					break;
				case Regra.Condicao.IGUAL:
					sb.append("AND "+getCampo(regra.getCampo())+" = ('%"+regra.getValor().toUpperCase()+"%') ");
					break;
			}
		}
		sb.append("AND l.fk_regras_id is null ");
		if(sugerir == 0 || sugerir == 1) {
			if(regraSugerida != null) {
				sb.append("AND (SELECT grupo_regras.id ");
				sb.append("FROM grupo_regras  "); 
				sb.append("WHERE l.campos @>  grupo_regras.campos ");
				if(sugerir == 1)
					sb.append("AND grupo_regras.cnpj_contabilidade = :cnpjContabilidade ");
				sb.append("AND grupo_regras.ativo = true ");
				sb.append("AND grupo_regras.contagem_regras > 2 ");
				sb.append("AND NOT EXISTS(SELECT 1 FROM regras r2 WHERE r2.fk_grupo_regras_id = grupo_regras.id AND r2.condicao = 2) "); 
				sb.append("ORDER BY grupo_regras.contagem_regras DESC, peso_regras DESC LIMIT 1) = :regraSugerida ");
			}
		}
		Query query = em.createNativeQuery(sb.toString());
		query.setParameter("contaMovimento", contaMovimento);
		query.setParameter("regraId", regraId);
		query.setParameter("cnpjEmpresa", cnpjEmpresa);
		if(sugerir == 0 || sugerir == 1 ) {
			if(regraSugerida != null) {
				query.setParameter("regraSugerida", regraSugerida);
				if(sugerir == 1)
					query.setParameter("cnpjContabilidade", cnpjContabilidade);
				}
		}
		return query.executeUpdate();
	}
    
    private String getCampo(String campo) {
		if (campo.contains("tipoPlanilha")) {
			campo = "tipo_planilha";
		}
		else if (campo.contains("portador")) {
			campo = "portador";
		}
		else if (campo.contains("descricao")) {
			campo = "descricao";
		}
		else if (campo.contains("documento")) {
			campo = "documento";
		}
		else if(campo.contains("nomeArquivo")) {
			campo = "nome_arquivo";
		}
		else if(campo.contains("tipoMovimento")) {
			campo = "tipo_movimento";
		}
    	return campo;
    }

    private List<Predicate> predicates(List<Regra> clauses, CriteriaBuilder cb, Root<Lancamento> root) { // @formatter:off
        final List<Predicate> predicates = new ArrayList<Predicate>();
        clauses.forEach((clause) -> {
            Path<String> path = root.get(clause.getCampo());
            switch (clause.getCondicao()) {
                case Regra.Condicao.CONTEM:
                    predicates.add(cb.like(unaccent(cb, path), MessageFormat.format("%{0}%", clause.getValor()).toUpperCase()));
                    break;
                case Regra.Condicao.NAO_CONTEM:
                    predicates.add(cb.notLike(unaccent(cb, path), MessageFormat.format("%{0}%", clause.getValor()).toUpperCase()));
                    break;
                case Regra.Condicao.COMECAO_COM:
                    predicates.add(cb.like(unaccent(cb, path), MessageFormat.format("%{0}", clause.getValor()).toUpperCase()));
                    break;
                case Regra.Condicao.IGUAL:
                    predicates.add(cb.equal(unaccent(cb, path), MessageFormat.format("{0}", clause.getValor()).toUpperCase()));
                    break;
            }
        });
        
        return predicates;
    }

    private Expression<String> unaccent(CriteriaBuilder cb, Path<String> path) {
        return cb.function("unaccent", String.class, cb.upper(path));
    }

	

}