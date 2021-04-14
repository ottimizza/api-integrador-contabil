package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;
import java.time.LocalDate;

import javax.inject.Inject;
import javax.validation.Valid;

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

    @PostMapping("/oic")
    public ResponseEntity<?> finalizaMesOIC(@Valid String cnpjEmpresa,
                                            @Valid Double saldo,
                                            @Valid String data,
                                            @Valid BigInteger bancoPadraoId) throws Exception {

        return ResponseEntity.ok(new GenericResponse<>(
                service.finalizaMesOIC(cnpjEmpresa, saldo, data, bancoPadraoId)
            ));
    }

    @GetMapping("/{cnpjEmpresa}")
    public ResponseEntity<?> buscaSaldo(@PathVariable String cnpjEmpresa) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
                service.buscaUltimoSaldo(cnpjEmpresa)
            ));
    }

    @GetMapping("/{cnpjEmpresa}/pendentes")
    public ResponseEntity<?> buscaRestantes(@PathVariable String cnpjEmpresa) throws Exception {
        return ResponseEntity.ok(new GenericResponse<>(
            service.contarPagamentosRecebimentosRestantes(cnpjEmpresa)
        ));
    }

    @GetMapping()
    public ResponseEntity<?> buscaPorBancoData(@Valid BigInteger bancoId,
                                               @Valid String data) throws Exception {
        return ResponseEntity.ok(new GenericResponse<SaldoBancosDTO>(
            service.buscaPorBancoData(bancoId, data)
        ));
    }
}