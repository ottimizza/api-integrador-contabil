package br.com.ottimizza.integradorcloud.client;

import java.math.BigInteger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.ottimizza.integradorcloud.domain.dtos.sfhistorico.SFHistorico;
import br.com.ottimizza.integradorcloud.domain.dtos.sfparticularidade.SFParticularidade;

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
	
}
