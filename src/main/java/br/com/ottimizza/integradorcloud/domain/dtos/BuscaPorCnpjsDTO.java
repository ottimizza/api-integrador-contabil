package br.com.ottimizza.integradorcloud.domain.dtos;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuscaPorCnpjsDTO {
	
	@NotNull
	private String cnpjContabilidade;
	
	@NotNull
	private String cnpjEmpresa;
}