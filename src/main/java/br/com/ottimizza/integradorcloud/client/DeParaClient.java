package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.ottimizza.integradorcloud.domain.dtos.depara.DeParaContaDTO;

@FeignClient(name = "DeParaService", url = "${clients.depara.base-url}")
public interface DeParaClient {

    @PostMapping("/api/v1/depara_contas")
    public HttpEntity<?> salvar(@RequestBody DeParaContaDTO deParaContaDTO, 
                                @RequestHeader("Authorization") String authorization);
                                
}
