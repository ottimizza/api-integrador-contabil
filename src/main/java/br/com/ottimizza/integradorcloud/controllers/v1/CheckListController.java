package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.checklist.CheckListRespostasDTO;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckList;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListPerguntas;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.CheckListService;

@RestController
@RequestMapping("/api/v1/checklist")
public class CheckListController {

	@Inject
	CheckListService checklistService;
	
	@GetMapping("/{tipo}")
	public ResponseEntity<?> buscaCheckList(@PathVariable("tipo") Short tipo)throws Exception {
		return ResponseEntity.ok(new GenericResponse<CheckList>(
				checklistService.buscaCheckList(tipo)
			));
	}
	
	// PERGUNTAS
	
	@PostMapping
	public ResponseEntity<?> salvaCheckListPergunta(@RequestBody CheckListPerguntas pergunta) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				checklistService.salvaPerguntas(pergunta)
			));
	}
	
	// RESPOSTAS 
	
	@PostMapping("/resposta")
	public ResponseEntity<?> salvaCheckListResposta(@RequestBody CheckListRespostasDTO resposta) throws Exception {
		return ResponseEntity.ok(new GenericResponse<CheckListRespostasDTO>(
				checklistService.salvaResposta(resposta)
			));
	}
	
	@GetMapping("/resposta")
	public ResponseEntity<?> getRespostas(@Valid CheckListRespostasDTO filtro,
										  @Valid PageCriteria criteria) throws Exception {
		return ResponseEntity.ok(new GenericPageableResponse<>(
				checklistService.getRespostas(filtro, criteria)
			));
	}
}
