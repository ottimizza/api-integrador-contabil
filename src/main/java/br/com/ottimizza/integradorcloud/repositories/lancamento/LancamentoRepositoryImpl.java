package br.com.ottimizza.integradorcloud.repositories.lancamento;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.QLancamento;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.Query;
// Sort
import com.querydsl.core.types.Order;
import org.springframework.data.domain.Sort;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

public class LancamentoRepositoryImpl implements LancamentoRepositoryCustom {

    private final String QLANCAMENTO_NAME = "lancamento";

    @PersistenceContext
    EntityManager em;

    private long totalElements;

    QLancamento lancamento = QLancamento.lancamento;

    public Page<Lancamento> fetchAll(LancamentoDTO filter, Pageable pageable) {
        JPAQuery<Lancamento> query = new JPAQuery<Lancamento>(em).from(lancamento);
        totalElements = filter(query, filter);
        sort(query, pageable, Lancamento.class, QLANCAMENTO_NAME);
        paginate(query, pageable);
        return new PageImpl<Lancamento>(query.fetch(), pageable, totalElements);
    }

    public long deleteAll(LancamentoDTO filter) {
        JPADeleteClause delete = new JPADeleteClause(em, lancamento);
        if (filter != null) {
            if (filter.getId() != null) {
                delete.where(lancamento.id.eq(filter.getId()));
            }
            if (filter.getCnpjContabilidade() != null && !filter.getCnpjContabilidade().isEmpty()) {
                delete.where(lancamento.cnpjContabilidade.like(filter.getCnpjContabilidade()));
            }
            if (filter.getCnpjEmpresa() != null && !filter.getCnpjEmpresa().isEmpty()) {
                delete.where(lancamento.cnpjEmpresa.like(filter.getCnpjEmpresa()));
            }
            if (filter.getIdRoteiro() != null && !filter.getIdRoteiro().isEmpty()) {
                delete.where(lancamento.idRoteiro.like(filter.getIdRoteiro()));
            }
            if (filter.getDescricao() != null && !filter.getDescricao().isEmpty()) {
                delete.where(lancamento.descricao.like("%" + filter.getDescricao() + "%"));
            }
        }
        return delete.execute();
    }

    private <T> long filter(JPAQuery<T> query, LancamentoDTO filter) {
        if (filter != null) {
            if (filter.getId() != null) {
                query.where(lancamento.id.eq(filter.getId()));
            }
            if (filter.getCnpjContabilidade() != null && !filter.getCnpjContabilidade().isEmpty()) {
                query.where(lancamento.cnpjContabilidade.like(filter.getCnpjContabilidade()));
            }
            if (filter.getCnpjEmpresa() != null && !filter.getCnpjEmpresa().isEmpty()) {
                query.where(lancamento.cnpjEmpresa.like(filter.getCnpjEmpresa()));
            }
            if (filter.getIdRoteiro() != null && !filter.getIdRoteiro().isEmpty()) {
                query.where(lancamento.idRoteiro.like(filter.getIdRoteiro()));
            }
            if (filter.getDescricao() != null && !filter.getDescricao().isEmpty()) {
                query.where(lancamento.descricao.like("%" + filter.getDescricao() + "%"));
            }
        }
        return query.fetchCount();
    }

    private <T> JPAQuery<T> paginate(JPAQuery<T> query, Pageable pageable) {
        query.limit(pageable.getPageSize());
        query.offset(pageable.getPageSize() * pageable.getPageNumber());
        return query;
    }

    private <T> JPAQuery<T> sort(JPAQuery<T> query, Pageable pageable, Class<T> clazz, String value) {
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, value);
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> propertyPath = entityPath.get(order.getProperty());
            query.orderBy(new OrderSpecifier(Order.valueOf(order.getDirection().name()), propertyPath));
        }
        return query;
    }

}