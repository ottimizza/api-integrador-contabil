package br.com.ottimizza.integradorcloud.controllers.v1;

import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.commands.lancamento.ImportacaoLancamentosRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.SearchCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LancamentoService;

import java.math.BigInteger;
import java.security.Principal;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController // @formatter:off
@RequestMapping("/api/v1/lancamentos")
public class LancamentoController {

    @Inject
    private LancamentoService lancamentoService;

    @GetMapping
    public ResponseEntity<?> fetchAll(@ModelAttribute SearchCriteria<LancamentoDTO> criteria, 
                                      Principal principal) throws Exception {
        GenericPageableResponse<LancamentoDTO> response = new GenericPageableResponse<LancamentoDTO>(
            lancamentoService.buscarTodos(criteria, principal)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@ModelAttribute SearchCriteria<LancamentoDTO> criteria, 
                                      Principal principal) throws Exception {
        GenericResponse response = new GenericResponse(
            lancamentoService.apagarTodos(criteria, principal)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchById(@PathVariable BigInteger id, 
                                       Principal principal) throws Exception {
        GenericResponse<LancamentoDTO> response = new GenericResponse<LancamentoDTO>(
            lancamentoService.buscarPorId(id, principal)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LancamentoDTO lancamento, 
                                    Principal principal) throws Exception {
        GenericResponse<LancamentoDTO> response = new GenericResponse<LancamentoDTO>(
            lancamentoService.salvar(lancamento, principal)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable BigInteger id, 
                                   @RequestBody LancamentoDTO lancamento, 
                                   Principal principal) throws Exception {
        GenericResponse<LancamentoDTO> response = new GenericResponse<LancamentoDTO>(
            lancamentoService.salvar(id, lancamento, principal)
        );
        return ResponseEntity.ok(response);
    }

    //
    //
    @PostMapping("/{id}/depara")
    public ResponseEntity<?> salvarTransacaoComoDePara(@PathVariable BigInteger id, @RequestParam String contaMovimento,
            OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(lancamentoService.salvarTransacaoComoDePara(id, contaMovimento, authentication));
    }

    @PostMapping("/{id}/outras_contas")
    public ResponseEntity<?> salvarTransacaoComoOutrasContas(@PathVariable BigInteger id,
            @RequestParam String contaMovimento, Principal principal) throws Exception {
        return ResponseEntity.ok(lancamentoService.salvarTransacaoComoOutrasContas(id, contaMovimento, principal));
    }

    @PostMapping("/{id}/ignorar")
    public ResponseEntity<?> salvarTransacaoComoIgnorar(@PathVariable BigInteger id, OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(lancamentoService.salvarTransacaoComoIgnorar(id, authentication));
    }

    //
    //
    @PostMapping("/importar")
    public ResponseEntity<?> importar(@RequestBody ImportacaoLancamentosRequest importacaoLancamentos,
            Principal principal) throws Exception {
        return ResponseEntity.ok(lancamentoService.importar(importacaoLancamentos, principal));
    }

}