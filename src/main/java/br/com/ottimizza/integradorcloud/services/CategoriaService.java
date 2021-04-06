package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.CategoriaDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.CategoriaMapper;
import br.com.ottimizza.integradorcloud.domain.models.Categoria;
import br.com.ottimizza.integradorcloud.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Inject
	CategoriaRepository repository;

	public CategoriaDTO salva(CategoriaDTO categoria) throws Exception {
		Categoria retorno = repository.save(CategoriaMapper.fromDTO(categoria));
		return CategoriaMapper.fromEntity(retorno);
	}

	public CategoriaDTO patch(BigInteger id, CategoriaDTO categoriaDto) throws Exception {
		Categoria categoria = repository.findById(id).orElse(null);
		Categoria retorno = repository.save(categoriaDto.patch(categoria));
		return CategoriaMapper.fromEntity(retorno);
	}

	public List<CategoriaDTO> buscaPorDescricao(String descricao) throws Exception {
		List<Categoria> categorias =  repository.buscaPorDescricao(descricao);
		return CategoriaMapper.fromEntities(categorias);
	}

	public String deletaPorId(BigInteger id) throws Exception {
		repository.deleteById(id);
		return "Categoria removida com sucesso!";
	}
}