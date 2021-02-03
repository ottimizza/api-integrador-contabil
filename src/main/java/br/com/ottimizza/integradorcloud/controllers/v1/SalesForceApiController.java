package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.SalesForceService;

@RestController
@RequestMapping("/api/v1/salesforce")
public class SalesForceApiController {

	@Inject
	SalesForceService salesForceService;

	@PatchMapping("/patch/{id}")
	public ResponseEntity<String> patch(@PathVariable BigInteger id,
				@RequestHeader("Authorization") String authorization) throws Exception {
		return ResponseEntity.ok(salesForceService.upsertRegra(id, authorization));
	}
	
	@GetMapping("/id")
	public ResponseEntity<?> getId(@Valid GrupoRegraDTO filter) throws Exception{
		return ResponseEntity.ok(salesForceService.getGrupoRegraIds(filter));
	}

	@PatchMapping("/historico/{id}")
	public ResponseEntity<?> patchHistorico(@PathVariable BigInteger id, 
											@RequestBody HistoricoDTO historico, 
											@RequestHeader("Authorization") String authorization) throws Exception {
		return ResponseEntity.ok(salesForceService.upsertHistorico(id, historico, authorization));
	}
	
	@GetMapping("/regras/exportar")
	public ResponseEntity<?> exportarRegras(@RequestBody GrupoRegraDTO grupoRegra,
										    OAuth2Authentication authentication) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				salesForceService.exportarRegras(grupoRegra, authentication)
			));
	}

}
