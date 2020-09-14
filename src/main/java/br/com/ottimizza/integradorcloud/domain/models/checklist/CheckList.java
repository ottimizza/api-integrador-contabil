package br.com.ottimizza.integradorcloud.domain.models.checklist;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CheckList {

	private BigInteger id;
	
	private CheckListDetalhes[] observacoes;
	
	private CheckListPerguntas[] perguntas;
}
