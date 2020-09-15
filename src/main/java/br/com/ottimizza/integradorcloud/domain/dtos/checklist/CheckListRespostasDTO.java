package br.com.ottimizza.integradorcloud.domain.dtos.checklist;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListRespostas;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class CheckListRespostasDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private BigInteger id;
	
	private BigInteger perguntaId;
	
	private BigInteger roteiroId;
	
	private String resposta;
	
	private String observacoes;

	public CheckListRespostas patch(CheckListRespostas respostaCheckList) {
		
		if(resposta != null && !resposta.equals(""))
			respostaCheckList.setResposta(resposta);
		
		if(observacoes != null && !observacoes.equals(""))
			respostaCheckList.setObservacoes(observacoes);
		
		return respostaCheckList;
	}
}
