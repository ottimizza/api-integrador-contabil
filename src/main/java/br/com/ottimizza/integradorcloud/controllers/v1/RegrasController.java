package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.RegraService;

@RestController
@RequestMapping("/api/v1/regras")
public class RegrasController {

    @Inject
    RegraService regraService;

    @PostMapping
    public ResponseEntity<?> criarRegra(GrupoRegra grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
            regraService.salvar(grupoRegraDTO, authentication)
        ));
    }

}