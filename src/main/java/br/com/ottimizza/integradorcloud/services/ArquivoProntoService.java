package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.commands.arquivo_pronto.ImportacaoArquivoPronto;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.ArquivoProntoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.ArquivoProntoMapper;
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
	
	public List<ArquivoPronto> importarArquivos(ImportacaoArquivoPronto importacaoArquivos) throws Exception {
		List<ArquivoPronto> retorno = new ArrayList();
		repository.deletaPorLoteECnpjs(importacaoArquivos.getArquivos().get(0).getLote(), importacaoArquivos.getCnpjEmpresa(), importacaoArquivos.getCnpjContabilidade());
		List<ArquivoPronto> arquivos = importacaoArquivos.getArquivos().stream().map((arquivo) -> {
			return ArquivoProntoMapper.fromDTO(arquivo).toBuilder()
					.cnpjContabilidade(importacaoArquivos.getCnpjContabilidade())
					.cnpjEmpresa(importacaoArquivos.getCnpjEmpresa())
					.build();
		}).collect(Collectors.toList());
		repository.saveAll(arquivos).forEach(retorno::add);
		return retorno;
	}

	public ArquivoProntoDTO patchArquivo(BigInteger id, ArquivoProntoDTO arquivoDTO) throws Exception {
		ArquivoPronto arquivo = repository.findById(id).orElseThrow(() -> new NoResultException("Arquivo protno nao encontrado!"));
		return ArquivoProntoMapper.fromEntity(repository.save(arquivoDTO.patch(arquivo)));
	}

	public Page<ArquivoPronto> buscaComFiltro(ArquivoProntoDTO filtro, PageCriteria criteria) throws Exception {
		return repository.buscaComFiltro(filtro, criteria);
	}

	public String deletaArquivo(BigInteger id) throws Exception {
		repository.deleteById(id);
		return "Arquivo pronto removido com sucesso!";
	}

}
