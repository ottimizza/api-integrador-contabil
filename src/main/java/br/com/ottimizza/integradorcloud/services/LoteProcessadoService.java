package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.commands.lote_processado.ImportacaoLoteProcessado;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.lote_processado.LoteProcessadoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.lote_processado.LoteProcessadoMapper;
import br.com.ottimizza.integradorcloud.domain.models.LoteProcessado;
import br.com.ottimizza.integradorcloud.repositories.lote_processado.LoteProcessadoRepository;

@Service
public class LoteProcessadoService {

	@Inject
	LoteProcessadoRepository repository;
	
	public LoteProcessadoDTO salva(LoteProcessadoDTO lote) throws Exception {
		LoteProcessado retorno = repository.save(LoteProcessadoMapper.fromDTO(lote));
		return LoteProcessadoMapper.fromEntity(retorno);
	}
	
	public List<LoteProcessado> importaLotes(ImportacaoLoteProcessado importacaoLotes) throws Exception {
		List<LoteProcessado> retorno =  new ArrayList<LoteProcessado>();
		List<LoteProcessado> lotes = importacaoLotes.getLotes().stream().map(lote -> {
			return LoteProcessadoMapper.fromDTO(lote).toBuilder()
					.cnpjEmpresa(importacaoLotes.getCnpjEmpresa())
					.cnpjContabilidade(importacaoLotes.getCnpjContabilidade())
					.build();
		}).collect(Collectors.toList());
		repository.saveAll(lotes).forEach(retorno::add);
		return retorno;
	}
	 
	public String deletaPorId(BigInteger id) throws Exception {
		repository.deleteById(id);
		return "Lote processado excluido com sucesso!";
	} 
	
	public Page<LoteProcessado> buscaComFiltro(LoteProcessadoDTO filtro, PageCriteria criteria) throws Exception {
		return repository.buscaComFiltro(filtro, criteria);
	}
	
}
