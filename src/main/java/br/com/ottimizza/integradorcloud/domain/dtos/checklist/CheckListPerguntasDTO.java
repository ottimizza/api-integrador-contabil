package br.com.ottimizza.integradorcloud.domain.dtos.checklist;

import java.io.Serializable;
import java.math.BigInteger;


import br.com.ottimizza.integradorcloud.domain.models.checklist.PerguntasOpcoesResposta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CheckListPerguntasDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigInteger id;
	
	private String descricao;
	
	private Short tipo;
	
	private Short tipoInput;
	
	private PerguntasOpcoesResposta[] opcoesResposta;
	
	private String sugestao;
	
	private String grupo;
}
