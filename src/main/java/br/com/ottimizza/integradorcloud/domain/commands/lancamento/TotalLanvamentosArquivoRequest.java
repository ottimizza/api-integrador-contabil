package br.com.ottimizza.integradorcloud.domain.commands.lancamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class TotalLanvamentosArquivoRequest {

	private long numeroLancamentos;
	
	private String nomeArquivo;
	
}
