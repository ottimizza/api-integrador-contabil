package br.com.ottimizza.integradorcloud.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.checklist.CheckListRespostasDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.checklist.CheckListMapper;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckList;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListPerguntas;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListRespostas;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListPerguntasRepository;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListRespostasRepository;

@Service
public class CheckListService {
	
	@Inject
	CheckListPerguntasRepository perguntasRepository;
	
	@Inject
	CheckListRespostasRepository respostasRepository;
	
	// PERGUNTAS 
	
	public CheckListPerguntas salvaPerguntas(CheckListPerguntas pergunta) throws Exception {
		return perguntasRepository.save(pergunta);
	}
	
	public CheckList buscaCheckList(Short tipoRoteiro) throws Exception {
		CheckList checkList = new CheckList();
		checkList.setPerguntas(perguntasRepository.buscaPorTipo(tipoRoteiro));
		checkList.setObservacoes(perguntasRepository.buscaObservacoes());
		return checkList;
	}
	
	public CheckListRespostasDTO salvaResposta(CheckListRespostasDTO respostaDTO) throws Exception {
		CheckListRespostas resposta = CheckListMapper.respostasFromDTO(respostaDTO);
		return CheckListMapper.respostasFromEntity(respostasRepository.save(resposta));
	}

}
