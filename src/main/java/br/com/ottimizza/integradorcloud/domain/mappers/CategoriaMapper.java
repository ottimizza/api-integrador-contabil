package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.CategoriaDTO;
import br.com.ottimizza.integradorcloud.domain.models.Categoria;

public class CategoriaMapper {

	public static Categoria fromDTO(CategoriaDTO categoria) {
		return Categoria.builder()
				.id(categoria.getId())
				.descricao(categoria.getDescricao())
			.build();
	}

	public static CategoriaDTO fromEntity(Categoria categoria) {
		return CategoriaDTO.builder()
				.id(categoria.getId())
				.descricao(categoria.getDescricao())
			.build();
	}

	public static List<CategoriaDTO> fromEntities(List<Categoria> categorias) {
        return categorias.stream().map(categoria -> fromEntity(categoria)).collect(Collectors.toList());
    }

} 