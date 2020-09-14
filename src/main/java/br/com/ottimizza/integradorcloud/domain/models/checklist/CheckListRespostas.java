package br.com.ottimizza.integradorcloud.domain.models.checklist;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CheckListRespostas {

	private BigInteger id;
	
	private BigInteger perguntaId;
	
	private BigInteger roteiroId;
	
	private String resposta;
	
	private String observacoes;
}
