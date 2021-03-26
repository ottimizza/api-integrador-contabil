package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;
import java.security.Principal;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.RoteiroService;

@RestController
@RequestMapping("/api/v1/roteiros")
public class RoteiroController {
	
	@Inject
	RoteiroService roteiroService;

	@GetMapping
	ResponseEntity<?> buscaRoteiros(@Valid RoteiroDTO filtro,
			  @Valid PageCriteria criteria,
			  Principal principal) throws Exception {
		return ResponseEntity.ok(new GenericPageableResponse<>(roteiroService.busca(filtro, criteria)));
	}
	
	@PostMapping
	ResponseEntity<?> criaRoteiro(@RequestBody RoteiroDTO roteiro,
								  OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(new GenericResponse<RoteiroDTO>(roteiroService.salva(roteiro, authentication)));
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
								   @RequestBody RoteiroDTO roteiro,
								   OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(new GenericResponse<RoteiroDTO>(roteiroService.patch(roteiroId, roteiro, authentication)));
	}
	
	@DeleteMapping("/{roteiroId}")
	ResponseEntity<?> deletaRoteiro(@PathVariable("roteiroId") BigInteger roteiroId) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(roteiroService.deleta(roteiroId)));
	}
	
	
}
