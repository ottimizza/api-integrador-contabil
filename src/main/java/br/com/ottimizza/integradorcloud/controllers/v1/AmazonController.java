package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.AmazonService;

@RestController
@RequestMapping("api/v1/amazon")
public class AmazonController {

	@Inject
	AmazonService amazonService;

	@GetMapping("/analizar")
	public ResponseEntity<?> analizarArquivo(@Valid String nomeArquivo) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				amazonService.analizarArquivo(nomeArquivo)
			));
	}

}
