package br.com.ottimizza.integradorcloud.client;

import java.math.BigInteger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFContabilidade;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFHistorico;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFParticularidade;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFRoteiro;

@FeignClient(name = "${salesforce.service.name}", url = "${salesforce.service.url}" )
public interface SalesForceClient {
	
	
	@PostMapping("/api/v1/salesforce/sobjects/Roteiros_vs_Contas__c/ID_Externo__c/{id}")
	public ResponseEntity<String> upsert(@PathVariable("id") BigInteger id ,
										 @RequestBody SFParticularidade particularidade,
										 @RequestHeader("Authorization") String authorization);
	
	
	@PostMapping("/api/v1/salesforce/sobjects/Roteiro_vs_Historio__c/ID_Externo__c/{id}")
	public ResponseEntity<String> upsertHistorico(@PathVariable("id") BigInteger id,
												  @RequestBody SFHistorico hisorico, 
												  @RequestHeader("Authorization")  String authorization);
	
	@PostMapping("/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/{id}")
	public ResponseEntity<String> upsertEmpresa(@PathVariable("id") String nomeResumido,
												@RequestBody SFEmpresa empresa, 
												@RequestHeader("Authorization")  String authorization);
	
	@PostMapping("/api/v1/salesforce/sobjects/Roteiros__c/Chave_OIC_Integracao__c/{id}")
	public ResponseEntity<String> upsertRoteiro(@PathVariable("id") String chaveOic,
												@RequestBody SFRoteiro roteiro, 
												@RequestHeader("Authorization")  String authorization);
	
	@PatchMapping("/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/{id}")
	public ResponseEntity<String> patchEmpresa(@PathVariable("id") String nomeResumido,
											   @RequestBody SFEmpresa empresa, 
											   @RequestHeader("Authorization")  String authorization);
		
	@GetMapping(value = "/api/v1/salesforce/sobjects/Contabilidade__c/CNPJ_Numeros__c/{cnpj}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<SFContabilidade> getContabilidade(@PathVariable("cnpj") String cnpj,
											  				@RequestHeader("Authorization")  String authorization);
	
	@GetMapping(value = "/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/{nomeResumido}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> getEmpresa(@PathVariable("nomeResumido") String nomeResumido,
											  				@RequestHeader("Authorization")  String authorization);

	@GetMapping(value = "/api/v1/salesforce/sobjects/Roteiro__c/Chave_OIC_Integracao__c/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<SFRoteiro> getRoteiro(@PathVariable("id") String chaveOic,
											  	@RequestHeader("Authorization")  String authorization);
	
	@GetMapping(value = "/execute_soql",  produces = {MediaType.APPLICATION_JSON_VALUE})
	 public ResponseEntity<?> executeSOQL(@RequestParam("soql") String soql,
	 				  					  @RequestHeader("Authorization")  String authorization);
	
}
