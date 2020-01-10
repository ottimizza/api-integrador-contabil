
package br.com.ottimizza.integradorcloud.repositories.grupo_regra;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;

public interface GrupoRegraRepositoryCustom {

    @Modifying
    @Transactional
    long apagarTodosPorCnpjEmpresa(String cnpjEmpresa);

}