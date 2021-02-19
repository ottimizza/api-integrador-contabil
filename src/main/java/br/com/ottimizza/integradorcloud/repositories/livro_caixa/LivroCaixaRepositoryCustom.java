package br.com.ottimizza.integradorcloud.repositories.livro_caixa;


import java.math.BigInteger;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.livro_caixa.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public interface LivroCaixaRepositoryCustom {

	Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria);
	
	GrupoRegra sugerirRegra(BigInteger livroCaixaId, String cnpjContabilidade, String cnpjEmpresa);

}

