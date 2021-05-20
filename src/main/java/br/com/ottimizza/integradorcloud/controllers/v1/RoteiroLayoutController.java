package br.com.ottimizza.integradorcloud.controllers.v1;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts.RoteiroLayout;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.RoteiroLayoutService;

@RestController
@RequestMapping("api/v1/roteiro_layout")
public class RoteiroLayoutController {

    @Inject
    RoteiroLayoutService service;

    @PostMapping
    public ResponseEntity<?> salva(@RequestBody RoteiroLayout roteiroLayout) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.salva(roteiroLayout)
            ));
    }
    
}
