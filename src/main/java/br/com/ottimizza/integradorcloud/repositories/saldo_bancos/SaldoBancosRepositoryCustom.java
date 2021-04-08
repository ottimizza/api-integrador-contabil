package br.com.ottimizza.integradorcloud.repositories.saldo_bancos;

import java.util.List;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.ViewSaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.ViewSaldoBancos;

public interface SaldoBancosRepositoryCustom {
    
    List<ViewSaldoBancos> buscaUltimoSaldoPorBancos(String cnpjEmpresa);

}
