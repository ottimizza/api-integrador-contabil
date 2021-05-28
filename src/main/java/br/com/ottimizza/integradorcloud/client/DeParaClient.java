package br.com.ottimizza.integradorcloud.client;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ottimizza.integradorcloud.domain.dtos.DeParaContaDTO;

@FeignClient(name = "${depara.service.name}", url = "${depara.service.url}")
public interface DeParaClient {

    @PostMapping("/api/v1/depara_contas")
    public HttpEntity<?> salvar(@RequestBody DeParaContaDTO deParaContaDTO, 
                                @RequestHeader("Authorization") String authorization);
                                

    @GetMapping("/api/v1/depara_contas")
    public HttpEntity<?> buscaDePara(@RequestParam("descricao") String descricao,
                                     @RequestParam("cnpjEmpresa") String cnpjEmpresa,
                                     @RequestParam("cnpjContabilidade") String cnpjContabilidade,
                                     @RequestHeader("Authorization") String authorization);
}
