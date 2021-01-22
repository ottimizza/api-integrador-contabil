package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.livro_caixa.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LivroCaixaService;

@RestController
@RequestMapping("/api/v1/livro_caixa")
public class LivroCaixaController {
	
	@Inject
	LivroCaixaService service;
	
	@GetMapping
	public ResponseEntity<?> buscaComFiltro(@Valid LivroCaixaDTO filtro,
										 	@Valid PageCriteria criteria) throws Exception {
		return ResponseEntity.ok(new GenericPageableResponse<LivroCaixa>(
				service.buscaComFiltro(filtro, criteria)
			));
	}
	
	@PostMapping
	public ResponseEntity<?> salvaLivroCaixa(@RequestBody LivroCaixaDTO livroCaixa) throws Exception {
		return ResponseEntity.ok(new GenericResponse<LivroCaixaDTO>(
				service.salva(livroCaixa)
			));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizaLivroCaixa(@PathVariable BigInteger id,
												@RequestBody LivroCaixaDTO livroCaixa) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.patch(id, livroCaixa)
			));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletaLivroCaixa(@PathVariable BigInteger id) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.deletaPorId(id)
			));
	}
}