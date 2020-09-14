package br.com.ottimizza.integradorcloud.domain.models.checklist;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class PerguntasOpcoesResposta {

	private BigInteger id;
	
	private String descricao;
	
	private String valor;
}
