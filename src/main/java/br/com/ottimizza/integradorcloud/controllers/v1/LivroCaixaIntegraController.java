package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LivroCaixaService;

@RestController
@RequestMapping("/integra/v1/livro_caixa")
public class LivroCaixaIntegraController {
	
	@Inject
	LivroCaixaService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> integraContabilidade(@PathVariable BigInteger id) {
		System.out.println(">>> X99 "+id);
		service.integraContabilidade(id);
		return ResponseEntity.ok().build();
	}
	
}
