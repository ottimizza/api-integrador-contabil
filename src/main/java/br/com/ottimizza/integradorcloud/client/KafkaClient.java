package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;


@FeignClient(name = "${kafka.service.name}", url = "${kafka.service.url}")
public interface KafkaClient {
    
    @PostMapping("/oic/v1/meucaixa")
    ResponseEntity<?> integradaLivrosCaixas(@RequestBody String livroCaixa);

}
