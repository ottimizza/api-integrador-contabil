package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.services.RoteiroService;

@RestController
@RequestMapping("/api/v1/roteiros")
public class RoteiroController {
	
	@Inject
	RoteiroService roteiroService;

	@PostMapping
	ResponseEntity<?> criaRoteiro(RoteiroDTO roteiro,
								  OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(null);
	}
	
	@PostMapping
	ResponseEntity<?> uploadPlanilha(@Valid SalvaArquivoRequest salvaArquivo,
									@RequestParam MultipartFile arquivo,
									OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(roteiroService.uploadPlanilha(salvaArquivo, arquivo, authentication));
	}
	
	
}
