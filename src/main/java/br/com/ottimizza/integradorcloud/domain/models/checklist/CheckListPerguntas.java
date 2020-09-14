package br.com.ottimizza.integradorcloud.domain.models.checklist;


import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class CheckListPerguntas {

	private BigInteger id;
	
	private String descricao;
	
	private Short tipo;
	
	private PerguntasOpcoesResposta opcoesResposta;
	
	private String valor;
	
	private String sugestao;
	
	private Integer grupo;
	
}
