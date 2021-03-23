package br.com.ottimizza.integradorcloud.repositories.livro_caixa;


import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public interface LivroCaixaRepositoryCustom {

	Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria);

}

