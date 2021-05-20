package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import br.com.ottimizza.integradorcloud.domain.models.checklist.PerguntasOpcoesResposta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor 
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class CheckListPerguntasDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigInteger id;
	
	private String descricao;
	
	private String tipo;
	
	private Short tipoInput;
	
	private PerguntasOpcoesResposta[] opcoesResposta;
	
	private String sugestao;
	
	private String grupo;

	private List<Long> perguntasRelacionadas;
}
