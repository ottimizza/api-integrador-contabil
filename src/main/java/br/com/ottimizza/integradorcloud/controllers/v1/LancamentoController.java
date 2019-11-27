package br.com.ottimizza.integradorcloud.controllers.v1;

import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.commands.lancamento.ImportacaoLancamentosRequest;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.services.LancamentoService;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/lancamentos")
public class LancamentoController {

    @Inject
    private LancamentoService lancamentoService;

    @GetMapping
    public ResponseEntity<?> fetchAll() throws Exception {
        return ResponseEntity.ok("[]");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchById(@PathVariable BigInteger id) throws Exception {
        return ResponseEntity.ok("[]");
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LancamentoDTO lancamento, Principal principal) throws Exception {
        return ResponseEntity.ok(lancamentoService.salvar(lancamento, principal));
    }

    @PostMapping("/importar")
    public ResponseEntity<?> importar(@RequestBody ImportacaoLancamentosRequest importacaoLancamentos,
                                      Principal principal) throws Exception {
        return ResponseEntity.ok(lancamentoService.importar(importacaoLancamentos, principal));
    }

}