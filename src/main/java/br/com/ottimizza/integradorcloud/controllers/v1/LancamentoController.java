package br.com.ottimizza.integradorcloud.controllers.v1;

import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/lancamentos")
public class LancamentoController {

    @GetMapping
    public ResponseEntity<?> fetchAll() throws Exception {
        return ResponseEntity.ok("[]");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchById(@PathVariable BigInteger id) throws Exception {
        return ResponseEntity.ok("[]");
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<LancamentoDTO> lancamentos) throws Exception {
        return ResponseEntity.ok(lancamentos);
    }

}