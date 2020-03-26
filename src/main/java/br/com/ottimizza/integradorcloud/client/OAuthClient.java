package br.com.ottimizza.integradorcloud.client;

import java.math.BigInteger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ottimizza.integradorcloud.domain.dtos.organization.OrganizationDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.user.UserDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;

@FeignClient(name = "${oauth.service.name}", url = "${oauth.service.url}") // @formatter:off
public interface OAuthClient {

    @GetMapping("/oauth/userinfo")
    public ResponseEntity<GenericResponse<UserDTO>> getUserInfo(@RequestHeader("Authorization") String authorization);

    @GetMapping("/api/v1/organizations")
    public ResponseEntity<GenericPageableResponse<OrganizationDTO>> buscarEmpresasPorCNPJ(@RequestParam("cnpj") String cnpjEmpresa, 
                                                                                          @RequestHeader("Authorization") String authorization);
    @GetMapping("/api/v1/organizations") 
    public ResponseEntity<GenericPageableResponse<OrganizationDTO>> buscaContabilidadeId(@RequestParam("cnpj") String cnpjContabilidade,
    																					 @RequestParam("type") Integer type,
    																					 @RequestHeader("Authorization") String authorization);
    																					 
    @GetMapping("/api/v1/organizations") 
    public ResponseEntity<GenericPageableResponse<OrganizationDTO>> buscaContabilidade(@RequestParam("cnpj") String cnpjContabilidade,
    																				   @RequestParam("type") Integer type,
    																				   @RequestParam("ignoreAccountingFilter") boolean ignore,
    																				   @RequestHeader("Authorization") String authorization);
    
    @GetMapping("/api/v1/organizations")
    public ResponseEntity<GenericPageableResponse<OrganizationDTO>> buscaEmpresa(@RequestParam("cnpj") String cnpjEmpresa,
    																			 @RequestParam("organizationId")BigInteger contabilidadeId,
    																			 @RequestParam("type") Integer type,
    																			 @RequestHeader("Authorization") String authorization);
    
    @PostMapping("/api/v1/organizations")
    public ResponseEntity<GenericPageableResponse<OrganizationDTO>> salvaEmpresa(@RequestBody OrganizationDTO organization, 
    																			 @RequestHeader("Authorization") String authorization);
    
}
