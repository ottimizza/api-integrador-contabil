package br.com.ottimizza.integradorcloud.repositories.banco;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancoDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.Banco;
import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;

public interface BancoRepositoryCustom {
    
    Page<Banco> buscaComFiltro(BancoDTO filtro, PageCriteria criteria);

    Page<BancosPadroes> buscaBancosPadroesComFiltro(BancoDTO filtro, PageCriteria criteria);
}
