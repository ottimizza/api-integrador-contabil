package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.empresa.EmpresaDTO;
import br.com.ottimizza.integradorcloud.services.EmpresaService;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController {

    @Inject
    EmpresaService empresaService;
    
    @GetMapping
    public ResponseEntity<?> buscarEmpresas(@Valid EmpresaDTO filter, OAuth2Authentication authentication) throws Exception {
        throw new Exception("message");
    }

}