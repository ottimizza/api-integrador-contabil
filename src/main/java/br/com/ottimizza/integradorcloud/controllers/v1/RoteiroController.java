package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.RoteiroService;

import java.math.BigInteger;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/roteiros")
public class RoteiroController {
	
	@Inject
	RoteiroService roteiroService;

	@GetMapping
	ResponseEntity<?> buscaTodos(@Valid RoteiroDTO filtro,
								 @Valid PageCriteria criteria,
								 Principal principal) throws Exception {
		return ResponseEntity.ok(new GenericPageableResponse<>(roteiroService.buscaTodos(filtro, criteria, principal)));
	}
	
	@PostMapping
	ResponseEntity<?> criaRoteiro(RoteiroDTO roteiro,
								  OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(new GenericResponse<RoteiroDTO>(roteiroService.salva(roteiro)));
	}
	
	@PostMapping("/{roteiroId}")
	ResponseEntity<?> uploadPlanilha(@PathVariable("roteiroId") BigInteger roteiroId,
									 @Valid SalvaArquivoRequest salvaArquivo,
									 @RequestParam("file") MultipartFile arquivo,
									 @RequestHeader("Authorization") String authorization) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(roteiroService.uploadPlanilha(roteiroId, salvaArquivo, arquivo, authorization)));
	}
	
	@PatchMapping("/{roteiroId}")
	ResponseEntity<?> patchRoteiro(@PathVariable("roteiroId") BigInteger roteiroId,
								   @RequestBody RoteiroDTO roteiro) throws Exception {
		return ResponseEntity.ok(new GenericResponse<RoteiroDTO>(roteiroService.patch(roteiroId, roteiro)));
	}
	
	@DeleteMapping("/{roteiroId}")
	ResponseEntity<?> deletaRoteiro(@PathVariable("roteiroId") BigInteger roteiroId) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(roteiroService.deleta(roteiroId)));
	}
	
	
}
