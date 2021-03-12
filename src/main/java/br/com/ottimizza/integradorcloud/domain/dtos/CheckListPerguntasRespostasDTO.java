package br.com.ottimizza.integradorcloud.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class CheckListPerguntasRespostasDTO {

	private String pergunta;

	private String resposta;

	private String observacao;

}