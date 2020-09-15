package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class Mapeamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean possuiCamposMapeados;
	
	private Boolean possuiPlanilhasExistentes;
	
	private MapeamentoCamposPlanilha mapeamentoCamposPlanilha;
	
}
