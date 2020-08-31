package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.services.RoteiroService;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/roteiros")
public class RoteiroController {
	
	@Inject
	RoteiroService roteiroService;

	@PostMapping
	ResponseEntity<?> criaRoteiro(RoteiroDTO roteiro,
								  OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(roteiroService.salva(roteiro));
	}
	
	@PostMapping("/{roteiroId}")
	ResponseEntity<?> uploadPlanilha(@PathVariable("roteiroId") BigInteger roteiroId,
									 @Valid SalvaArquivoRequest salvaArquivo,
									 @RequestParam MultipartFile arquivo,
									 OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(roteiroService.uploadPlanilha(roteiroId, salvaArquivo, arquivo, authentication));
	}
	
	
}
