package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import feign.Param;

@FeignClient(name = "${s1.service.name}", url = "${s1.service.url}")
public interface OttimizzaS1Client {

	@GetMapping("/contabil-server/roteiro.jsp?idRoteiro={idRoteiro}&codigoRoteiro={codRoteiro}&empresa={empresa}&tipo={tipo}&path=Deploy")
	public void exportarRegras(@Param("idRoteiro") String idRoteiro, 
			                   @Param("codRoteiro") String codigoRoteiro, 
			                   @Param("empresa") String empresa, 
			                   @Param("tipo") String tipoConta);
	
}
