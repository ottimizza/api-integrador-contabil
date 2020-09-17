package br.com.ottimizza.integradorcloud.repositories.roteiro;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;

public interface RoteiroRepositoryCustom {

	Page<Roteiro> buscaRoteiros(RoteiroDTO filtro, PageCriteria criteria);
}
