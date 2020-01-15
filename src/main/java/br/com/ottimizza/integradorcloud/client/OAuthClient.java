package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${oauth.service.name}", url = "${oauth.service.url}") // @formatter:off
public interface OAuthClient {

    @GetMapping("/oauth/userinfo")
    public HttpEntity<?> getUserInfo(@RequestHeader("Authorization") String authorization);

    @GetMapping("/api/v1/organizations")
    public HttpEntity<?> buscarEmpresasPorCNPJ(@RequestParam("cnpj") String cnpjEmpresa, 
                                               @RequestHeader("Authorization") String authorization);
                                
}
