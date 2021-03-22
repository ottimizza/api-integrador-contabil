package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
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
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> integraContabilidade(@PathVariable BigInteger id) {
		return ResponseEntity.ok(new GenericResponse<LivroCaixaDTO>(service.integraContabilidade(id)));
	}
	
}
