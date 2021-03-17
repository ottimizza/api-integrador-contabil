package br.com.ottimizza.integradorcloud.domain.models.checklist;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class PerguntasOpcoesResposta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String descricao;
	
	private String valor;
}
