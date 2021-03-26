package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;

import com.amazonaws.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.SaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.SaldoBancosService;

@RestController
@RequestMapping("/api/v1/saldo_bancos")
public class SaldoBancosController {

    @Inject
    SaldoBancosService service;


    @PostMapping
    public ResponseEntity<?> salvaSaldo(@RequestBody SaldoBancosDTO saldo) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.salvaSaldo(saldo)
            ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscaSaldo(@PathVariable BigInteger bancoId) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.buscaUltimoSaldo(bancoId)
            ));
    }
}