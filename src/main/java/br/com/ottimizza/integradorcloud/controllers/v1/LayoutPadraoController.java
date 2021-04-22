package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LayoutPadraoDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LayoutPadraoService;

@RestController
@RequestMapping("/api/v1/layout_padrao")
public class LayoutPadraoController {

    @Inject
    LayoutPadraoService service;

    @GetMapping
    ResponseEntity<?> buscaComFiltro(@Valid LayoutPadraoDTO filtro,
                                     @Valid PageCriteria criteria) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.buscaComFiltro(filtro, criteria)
            ));
    }

    @PostMapping
    ResponseEntity<?> salvaLayout(@RequestBody LayoutPadraoDTO layout) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.save(layout)
            ));
    }
    
    @PatchMapping("{id}")
    ResponseEntity<?> patchLayout(@PathVariable("id") BigInteger id,
                                  @RequestBody LayoutPadraoDTO layoutDto) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.patch(id, layoutDto)
            ));
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteById(@PathVariable("id") BigInteger id) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.deletaPorId(id)
            ));
    }

}
