package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

import com.querydsl.jpa.impl.JPAQuery;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.QLancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;

public class LancamentoRepositoryImpl implements LancamentoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    long totalElements = 0;
    
    QLancamento lancamento = QLancamento.lancamento;

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

    public Page<Lancamento> buscarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa, Pageable pageable,
            Principal principal) {
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