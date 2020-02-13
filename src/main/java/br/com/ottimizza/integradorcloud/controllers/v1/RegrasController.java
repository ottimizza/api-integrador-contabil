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
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.RegraService;

@RestController
@RequestMapping("/api/v1/regras")
public class RegrasController {

    @Inject
    RegraService regraService;

    @GetMapping
    public ResponseEntity<?> buscarRegras(@Valid GrupoRegraDTO filter, 
                                          @Valid PageCriteria pageCriteria, 
                                          OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericPageableResponse<GrupoRegraDTO>(
            regraService.buscarRegras(filter, pageCriteria, authentication)
        ));
    }

    @PostMapping
    public ResponseEntity<?> criarRegra(@RequestBody GrupoRegraDTO grupoRegraDTO, OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
            regraService.salvar(grupoRegraDTO, authentication)
        ));
    }

}