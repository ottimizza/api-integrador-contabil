package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class Mapeamento {

	private Boolean possuiCamposMapeados;
	
	private Boolean possuiPlanilhasExistentes;
	
	private MapeamentoCamposPlanilha mapeamentoCamposPlanilha;
	
}
