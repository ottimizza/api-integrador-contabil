package br.com.ottimizza.integradorcloud.domain.mappers.checklist;

import br.com.ottimizza.integradorcloud.domain.dtos.checklist.CheckListPerguntasDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.checklist.CheckListRespostasDTO;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListPerguntas;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListRespostas;

public class CheckListMapper {

	//CHECKLIST RESPOSTAS
	
	public static CheckListRespostas respostasFromDTO(CheckListRespostasDTO resposta) {
		return CheckListRespostas.builder()
				.id(resposta.getId())
				.perguntaId(resposta.getPerguntaId())
				.roteiroId(resposta.getRoteiroId())
				.resposta(resposta.getResposta())
				.observacoes(resposta.getObservacoes())
			.build();
	}
	
	public static CheckListRespostasDTO respostasFromEntity(CheckListRespostas resposta) {
		return CheckListRespostasDTO.builder()
				.id(resposta.getId())
				.perguntaId(resposta.getPerguntaId())
				.roteiroId(resposta.getRoteiroId())
				.resposta(resposta.getResposta())
				.observacoes(resposta.getObservacoes())
			.build();
	}
	
	//CHECKLIST PERGUNTAS
	
	public static CheckListPerguntas perguntasFromDTO(CheckListPerguntasDTO pergunta) {
		return CheckListPerguntas.builder()
				.id(pergunta.getId())
				.descricao(pergunta.getDescricao())
				.grupo(pergunta.getGrupo())
				.opcoesResposta(pergunta.getOpcoesResposta())
				.sugestao(pergunta.getSugestao())
				.tipo(pergunta.getTipo())
				.tipoInput(pergunta.getTipoInput())
			.build();
	}
	
	public static CheckListPerguntasDTO perguntasFromEntity(CheckListPerguntas pergunta) {
		return CheckListPerguntasDTO.builder()
				.id(pergunta.getId())
				.descricao(pergunta.getDescricao())
				.grupo(pergunta.getGrupo())
				.opcoesResposta(pergunta.getOpcoesResposta())
				.sugestao(pergunta.getSugestao())
				.tipo(pergunta.getTipo())
				.tipoInput(pergunta.getTipoInput())
			.build();
	}
}
