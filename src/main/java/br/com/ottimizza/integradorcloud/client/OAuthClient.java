package br.com.ottimizza.integradorcloud.client;

import java.math.BigInteger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ottimizza.integradorcloud.domain.dtos.OrganizationDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
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
    
    @GetMapping("/api/v1/organizations/{id}")
    public ResponseEntity<GenericResponse<OrganizationDTO>> buscaContabilidadePorId(@PathVariable("id") BigInteger id,
    																				@RequestHeader("Authorization") String authorization);
   
    @PostMapping("/api/v1/organizations")
    public ResponseEntity<GenericResponse<OrganizationDTO>> salvaEmpresa(@RequestBody OrganizationDTO organization,
    																	 @RequestHeader("Authorization") String authorization);
    
    @PatchMapping("/api/v1/organizations/{id}")
    public HttpEntity<OrganizationDTO> patchOrganization(@PathVariable("id") BigInteger id, 
    													 @RequestBody OrganizationDTO organizationDTO, 
    													 @RequestHeader("Authorization") String authorization);																			 
    
    @GetMapping("/api/v1/users/{organizationId}/has_phone")
    public HttpEntity<GenericResponse<UserDTO>> getUserByOrganizationIdAndPhone(@PathVariable BigInteger organizationId,
                                                       @RequestHeader("Authorization") String authorization);

    @GetMapping("/api/v1/users/{organizationId}/organization")
    public HttpEntity<GenericResponse<UserDTO>> getUserByOrganizationId(@PathVariable BigInteger organizationId,
                                                       @RequestHeader("Authorization") String authorization);    

}
