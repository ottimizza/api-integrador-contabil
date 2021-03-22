package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;


import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancosPadroesDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.BancosPadroesService;

@RestController
@RequestMapping("/api/v1/bancos_padroes")
public class BancosPadroesController {

    @Inject
    BancosPadroesService service;
    
    @PostMapping
    public ResponseEntity<?> salvaBancosPadroes(@RequestBody BancosPadroes banco) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.salva(banco)
            ));
    }

    @GetMapping()
    public ResponseEntity<?> buscaBancosPadroes(@Valid BancosPadroesDTO filtro,
                                                @Valid PageCriteria criteria,
                                                OAuth2Authentication authentication) throws Exception {

        return ResponseEntity.ok(new GenericResponse<>(
                service.buscarBancos(filtro, criteria)
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaPorId(@PathVariable BigInteger id) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.deletaPorId(id)
            ));
    }

}
