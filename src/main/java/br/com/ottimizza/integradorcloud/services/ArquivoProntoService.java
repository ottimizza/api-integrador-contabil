package br.com.ottimizza.integradorcloud.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.aquivo_pronto.ArquivoProntoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.arquivo_pronto.ArquivoProntoMapper;
import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;
import br.com.ottimizza.integradorcloud.repositories.arquivo_pronto.ArquivoProntoRepository;

@Service
public class ArquivoProntoService {
	
	
	@Inject
	ArquivoProntoRepository repository;
	
	public ArquivoProntoDTO salvaArquivo(ArquivoProntoDTO arquivo) throws Exception {
		ArquivoPronto retorno = repository.save(ArquivoProntoMapper.fromDTO(arquivo));
		return ArquivoProntoMapper.fromEntity(retorno);
	}

}
