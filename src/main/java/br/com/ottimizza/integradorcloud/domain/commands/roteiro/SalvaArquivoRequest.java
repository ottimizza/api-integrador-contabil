package br.com.ottimizza.integradorcloud.domain.commands.roteiro;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class SalvaArquivoRequest {

	private String cnpjContabilidade;
	
	private String cnpjEmpresa;
	 
	private String applicationId;
	
}
