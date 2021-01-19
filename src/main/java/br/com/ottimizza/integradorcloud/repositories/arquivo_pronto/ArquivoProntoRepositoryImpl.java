package br.com.ottimizza.integradorcloud.repositories.arquivo_pronto;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.aquivo_pronto.ArquivoProntoDTO;
import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;

public class ArquivoProntoRepositoryImpl implements ArquivoProntoRepositoryCustom{

	@Override
	public Page<ArquivoPronto> buscaComFiltro(ArquivoProntoDTO filtro, PageCriteria criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM arquivo_pronto ap ");
		sql.append("WHERE ap.cnpj_contabilidade = :cnpjContabilidade ");
		sql.append("AND ap.cnpj_empresa = :cnpjEmpresa");
		if(filtro.getCodigoHistorico() != null && !filtro.getCodigoHistorico().equals(""))
			sql.append("AND ap.codigo_historico ILIKE('%:codigoHistorico%')");
		if(filtro.getContaCredito() != null && !filtro.getContaCredito().equals(""))
			sql.append("AND ap.conta_credito ILIKE('%:contaCredito%')");
		if(filtro.getContaDebito() != null && !filtro.getContaDebito().equals(""))
			sql.append("AND ap.conta_debito ILIKE('%:contaDebito%')");
		return null;
	}

}
