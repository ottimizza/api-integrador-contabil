package br.com.ottimizza.integradorcloud.repositories.saldo_bancos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.ottimizza.integradorcloud.domain.models.banco.ViewSaldoBancos;

public class SaldoBancosRepositoryImpl implements SaldoBancosRepositoryCustom{

    @PersistenceContext
    EntityManager em;

    @Override
    public List<ViewSaldoBancos> buscaUltimoSaldoPorBancos(String cnpjEmpresa) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT view_saldo_bancos.id, view_saldo_bancos.fk_banco_id as banco, view_saldo_bancos.data, view_saldo_bancos.saldo, view_saldo_bancos.cnpj_empresa "
                    +"FROM view_saldo_bancos "
                    +"WHERE view_saldo_bancos.cnpj_empresa = :cnpjEmpresa  ");

        Query query = em.createNativeQuery(sql.toString(), ViewSaldoBancos.class);
        query.setParameter("cnpjEmpresa", cnpjEmpresa);
        return query.getResultList();
    }
    
}
