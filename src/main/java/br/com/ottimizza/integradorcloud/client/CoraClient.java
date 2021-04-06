package br.com.ottimizza.integradorcloud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.ottimizza.integradorcloud.domain.dtos.cora.empresa.EmpresaCoraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.cora.retornos.RetornoPostEmpresa;

@FeignClient(name = "${cora.service.name}", url = "${cora.service.url}")
public interface CoraClient {
    

    @PostMapping("/oauth/token")
    public ResponseEntity<?> getAuthorization();

    @PostMapping("/registrations")
    public ResponseEntity<RetornoPostEmpresa> salvaEmpresaCora(@RequestBody EmpresaCoraDTO empresa,
                                                               @RequestHeader("Authorization") String authorization,
                                                               @RequestHeader("Idempotency-Key") String idempotencyKey);
}
