package br.com.ottimizza.integradorcloud.repositories.livro_caixa;


import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public interface LivroCaixaRepositoryCustom { //LivroCaixaRepositoryImpl

	Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria);

	GrupoRegra sugerirRegra(BigInteger livroCaixaId, String cnpjContabilidade, String cnpjEmpresa);

	List<LivroCaixaDTO> sugerirLancamento(String cnpjContabilidade, String cnpjEmpresa, Double valor, String data);
}

