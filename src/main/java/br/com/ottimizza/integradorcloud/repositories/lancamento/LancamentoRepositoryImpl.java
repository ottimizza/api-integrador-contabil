package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.lang.reflect.Field;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.Regra;

public class LancamentoRepositoryImpl implements LancamentoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    long totalElements = 0;

    public Long contarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Lancamento> root = query.from(Lancamento.class);

        query.select(cb.count(query.from(Lancamento.class))).where(cb.equal(root.get("cnpjEmpresa"), cnpjEmpresa));
        for (Regra regra : regras) {
            filter(regra, query, cb, root, Long.class);
        }
        return em.createQuery(query).getSingleResult();
    }

    public Page<Lancamento> buscarLancamentosPorRegra(List<Regra> regras, String cnpjEmpresa, Pageable pageable,
            Principal principal) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Lancamento> query = cb.createQuery(Lancamento.class);
        Root<Lancamento> root = query.from(Lancamento.class);

        query.select(root).where(cb.equal(root.get("cnpjEmpresa"), cnpjEmpresa));

        for (Regra regra : regras) {
            filter(query, cb, root, regra);
        }

        TypedQuery<Lancamento> typedQuery = em.createQuery(query);

        return new PageImpl<>(typedQuery.getResultList(), pageable, contarLancamentosPorRegra(regras, cnpjEmpresa));
    }

    private CriteriaQuery<Lancamento> filter(CriteriaQuery<Lancamento> query, CriteriaBuilder cb, Root<Lancamento> root,
            Regra regra) {
        Path<String> path = root.get(regra.getCampo());
        switch (regra.getCondicao()) {
        case Regra.Condicao.CONTEM:
            query.where(cb.like(path, MessageFormat.format("%{0}%", regra.getValor())));
            break;
        case Regra.Condicao.NAO_CONTEM:
            query.where(cb.notLike(path, MessageFormat.format("%{0}%", regra.getValor())));
            break;
        case Regra.Condicao.COMECAO_COM:
            query.where(cb.like(path, MessageFormat.format("%{0}", regra.getValor())));
            break;
        case Regra.Condicao.IGUAL:
            query.where(cb.equal(path, MessageFormat.format("{0}", regra.getValor())));
            break;
        }
        return query;
    }

    private <T> CriteriaQuery<T> filter(Regra regra, CriteriaQuery<T> query, CriteriaBuilder cb, Root<Lancamento> root,
            Class<T> clazz) {
        Path<String> path = root.get(regra.getCampo());
        switch (regra.getCondicao()) {
        case Regra.Condicao.CONTEM:
            query.where(cb.like(path, MessageFormat.format("%{0}%", regra.getValor())));
            break;
        case Regra.Condicao.NAO_CONTEM:
            query.where(cb.notLike(path, MessageFormat.format("%{0}%", regra.getValor())));
            break;
        case Regra.Condicao.COMECAO_COM:
            query.where(cb.like(path, MessageFormat.format("%{0}", regra.getValor())));
            break;
        case Regra.Condicao.IGUAL:
            query.where(cb.equal(path, MessageFormat.format("{0}", regra.getValor())));
            break;
        }
        return query;
    }

    private Field getField(String fieldName) throws NoSuchFieldException {
        return Lancamento.class.getField(fieldName);
    }

    private Object getFieldType(Field field) {
        return field.getType();
    }

    private Object getFieldValue(Field field, Lancamento instance) throws IllegalAccessException {
        return field.get(instance);
    }

}