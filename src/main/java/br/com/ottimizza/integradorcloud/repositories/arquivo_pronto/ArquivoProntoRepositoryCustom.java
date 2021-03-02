package br.com.ottimizza.integradorcloud.repositories.arquivo_pronto;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.ArquivoProntoDTO;
import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;

public interface ArquivoProntoRepositoryCustom {

	Page<ArquivoPronto> buscaComFiltro(ArquivoProntoDTO filtro, PageCriteria criteria);
	
}
