package br.com.ottimizza.integradorcloud.domain.commands.roteiro;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class SalvaArquivoRequest {

	private String cnpjContabilidade;
	
	@NotBlank
	private String cnpjEmpresa;
	 
	@NotBlank
	private String applicationId;
	
}
