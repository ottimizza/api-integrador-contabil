package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.domain.models.QGrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.QLancamento;
import br.com.ottimizza.integradorcloud.domain.models.QRegra;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.Query;
// Sort
import com.querydsl.core.types.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

public class GrupoRegraRepositoryImpl implements GrupoRegraRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    QGrupoRegra grupoRegra = QGrupoRegra.grupoRegra;

    QRegra regra = QRegra.regra;

    @Modifying
    @Transactional
    public long apagarTodosPorCnpjEmpresa(String cnpjEmpresa) {
        this.apagarRegraPorCnpjEmpresa(cnpjEmpresa);
        return this.apagarGrupoRegraPorCnpjEmpresa(cnpjEmpresa);
    }

    @Modifying
    @Transactional
    private long apagarGrupoRegraPorCnpjEmpresa(String cnpjEmpresa) {
        JPADeleteClause delete = new JPADeleteClause(em, grupoRegra);
        delete.where(grupoRegra.arquivo.cnpjEmpresa.eq(cnpjEmpresa));
        return delete.execute();
    }

    @Modifying
    @Transactional
    private long apagarRegraPorCnpjEmpresa(String cnpjEmpresa) {
        JPADeleteClause delete = new JPADeleteClause(em, regra);
        delete.where(regra.grupoRegra.arquivo.cnpjEmpresa.eq(cnpjEmpresa));
        return delete.execute();
    }

}
