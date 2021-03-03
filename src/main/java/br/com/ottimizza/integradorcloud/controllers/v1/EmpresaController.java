package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.EmpresaDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.EmpresaService;

@RestController
@RequestMapping("/api/v1/empresas")
public class EmpresaController {

    @Inject
    EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<?> buscarEmpresas(@Valid EmpresaDTO filter, 
                                            @Valid PageCriteria pageCriteria, 
                                            OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericPageableResponse<EmpresaDTO>(
            empresaService.buscarEmpresas(filter, pageCriteria, authentication)
        ));
    }
    
    @PostMapping
    public ResponseEntity<?> salvaEmpresas(@RequestBody EmpresaDTO empresa,
    									   OAuth2Authentication authentication) throws Exception {
    	return ResponseEntity.ok(new GenericResponse<EmpresaDTO>(
    			empresaService.salvar(empresa, authentication)
    		));
    }

}