package br.com.ottimizza.integradorcloud.domain.commands.lancamento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PorcentagemLancamentosRequest {

	long numeroLancamentosRestantes;
	
	long totalLancamentos;
	
}
