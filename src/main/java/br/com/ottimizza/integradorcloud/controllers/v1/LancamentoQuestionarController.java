package br.com.ottimizza.integradorcloud.controllers.v1;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LancamentoService;

@RestController
@RequestMapping("/questionar/v1/lancamentos")
public class LancamentoQuestionarController {

    @Inject
    LancamentoService lancamentoService;

    @PatchMapping("/{uuid}")
    public ResponseEntity<?> salvarQuestionamento(@PathVariable UUID uuid,
                                                  @RequestBody LancamentoDTO lancamentoDTO) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                lancamentoService.salvarQuestionamento(uuid, lancamentoDTO)
            ));
    }
    
}
