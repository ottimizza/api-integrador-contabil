package br.com.ottimizza.integradorcloud.domain.dtos;

import java.math.BigInteger;

import br.com.ottimizza.integradorcloud.domain.models.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class CategoriaDTO {

	private BigInteger id;

	private String descricao;

	public Categoria patch(Categoria categoria) {

		if(descricao != null && !descricao.equals(""))
			categoria.setDescricao(descricao);

		return categoria;
	}
}
