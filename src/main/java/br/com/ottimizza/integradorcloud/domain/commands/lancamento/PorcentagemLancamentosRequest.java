package br.com.ottimizza.integradorcloud.domain.commands.lancamento;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PorcentagemLancamentosRequest {

	long numeroLancamentosRestantes;
	
	long totalLancamentos;
	
}
