package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import feign.Param;

@FeignClient(name = "${s5.service.name}", url = "${s5.service.url}")
public interface OttimizzaS5Client {

	@GetMapping("/contabil-server/roteiro.jsp?idRoteiro={idRoteiro}&codigoRoteiro={codRoteiro}&empresa={empresa}&tipo={tipo}&path=Deploy")
	public void exportarRegras(@PathVariable("idRoteiro") String idRoteiro, 
								 @PathVariable("codRoteiro") String codigoRoteiro, 
								 	@PathVariable("empresa") String empresa, 
			  @PathVariable("tipo") String tipoConta);
	
}
