package br.com.ottimizza.integradorcloud.repositories.lote_processado;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.lote_processado.LoteProcessadoDTO;
import br.com.ottimizza.integradorcloud.domain.models.LoteProcessado;

public interface LoteProcessadoRepositoryCustom {

	Page<LoteProcessado> buscaComFiltro(LoteProcessadoDTO filtro, PageCriteria criteria);
	
}
