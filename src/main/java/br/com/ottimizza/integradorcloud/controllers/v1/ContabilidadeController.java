package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.ContabilidadeDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.ContabilidadeService;

@RestController
@RequestMapping("/api/v1/contabilidades")
public class ContabilidadeController {
    
    @Inject
    ContabilidadeService contabilidadeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscaPorId(@PathVariable("id") BigInteger id) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                contabilidadeService.buscaPorId(id)
            ));
    }

    @GetMapping("/{oauthId}/oauth")
    public ResponseEntity<?> buscaPorIdOauth(@PathVariable("oauthId") BigInteger oauthId) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                contabilidadeService.buscaPorOauthId(oauthId)
            ));
    }

    @PostMapping()
    public ResponseEntity<?> salvaContabilidade(@RequestBody ContabilidadeDTO contabilidadeDTO) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                contabilidadeService.salvaContabilidade(contabilidadeDTO)
            ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchContabilidade(@PathVariable BigInteger id, 
                                                @RequestBody ContabilidadeDTO contabilidadeDTO) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                contabilidadeService.patchContabilidade(id, contabilidadeDTO)
            ));                                              
    }

}
