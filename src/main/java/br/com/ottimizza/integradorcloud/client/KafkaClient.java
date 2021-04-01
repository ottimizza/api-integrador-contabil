package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "${kafka.service.name}", url = "${kafka.service.url}")
public interface KafkaClient {
    
    @PostMapping(value = "/oic/v1/meucaixa/list", consumes={"application/json"})
    void integradaLivrosCaixas(@RequestBody String livrosCaixas);

}
