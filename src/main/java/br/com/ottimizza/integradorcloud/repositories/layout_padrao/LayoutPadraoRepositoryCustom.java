package br.com.ottimizza.integradorcloud.repositories.layout_padrao;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LayoutPadraoDTO;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;

public interface LayoutPadraoRepositoryCustom {
    
    Page<LayoutPadrao> buscaComFiltro(LayoutPadraoDTO filtro, PageCriteria criteria);

}
