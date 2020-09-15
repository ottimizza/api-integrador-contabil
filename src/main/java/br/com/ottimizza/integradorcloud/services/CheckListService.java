package br.com.ottimizza.integradorcloud.services;


import javax.inject.Inject;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
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
	

	public CheckList buscaCheckList(Short tipoRoteiro) throws Exception {
		CheckList checkList = new CheckList();
		checkList.setPerguntas(perguntasRepository.buscaPorTipo(tipoRoteiro));
		checkList.setObservacoes(perguntasRepository.buscaObservacoes());
		return checkList;
	}
	
	// PERGUNTAS 
	
	public CheckListPerguntas salvaPerguntas(CheckListPerguntas pergunta) throws Exception {
		return perguntasRepository.save(pergunta);
	}
	
	// RESPOSTAS
	
	public CheckListRespostasDTO salvaResposta(CheckListRespostasDTO respostaDTO) throws Exception {
		CheckListRespostas respostaDb = null;
		try {
			respostaDb = respostasRepository.checkResposta(respostaDTO.getRoteiroId(), respostaDTO.getPerguntaId());
		}catch(Exception ex) { } 
		if(respostaDb != null)
			return CheckListMapper.respostasFromEntity(respostasRepository.save(respostaDTO.patch(respostaDb)));
		
		else {
			CheckListRespostas resposta = CheckListMapper.respostasFromDTO(respostaDTO);
			return CheckListMapper.respostasFromEntity(respostasRepository.save(resposta));
		}
	}
	
	public Page<CheckListRespostasDTO> getRespostas(CheckListRespostasDTO filtro, PageCriteria pageCriteria) throws Exception {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<CheckListRespostas> example = Example.of(CheckListMapper.respostasFromDTO(filtro), matcher);
		
        return respostasRepository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize()))
        										.map(CheckListMapper::respostasFromEntity);
	}

}
