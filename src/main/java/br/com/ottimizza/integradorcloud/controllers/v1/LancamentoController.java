package br.com.ottimizza.integradorcloud.controllers.v1;

import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.commands.lancamento.ImportacaoLancamentosRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.arquivo.ArquivoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LancamentoService;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<?> fetchAll(@Valid LancamentoDTO filter, @Valid PageCriteria pageCriteria, Principal principal) throws Exception {
        Page<LancamentoDTO> page = lancamentoService.buscarTodos(filter, pageCriteria, principal);
        return ResponseEntity.ok(new GenericPageableResponse<>(page));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LancamentoDTO lancamento, OAuth2Authentication authentication) throws Exception {
        LancamentoDTO lancamentoDTO = lancamentoService.salvar(lancamento, authentication);
        return ResponseEntity.ok(new GenericResponse<LancamentoDTO>(lancamentoDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@Valid LancamentoDTO filter, @Valid PageCriteria pageCriteria, 
                                       @RequestParam(defaultValue = "false", required = false) boolean limparRegras,
                                       Principal principal) throws Exception {
        String message = lancamentoService.apagarTodos(filter, pageCriteria, limparRegras, principal);
        return ResponseEntity.ok(new GenericResponse<>(message));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchById(@PathVariable BigInteger id, Principal principal) throws Exception {
        LancamentoDTO lancamentoDTO = lancamentoService.buscarPorId(id, principal);
        return ResponseEntity.ok(new GenericResponse<LancamentoDTO>(lancamentoDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable BigInteger id, @RequestBody LancamentoDTO lancamento, Principal principal) throws Exception {
        LancamentoDTO lancamentoDTO = lancamentoService.salvar(id, lancamento, principal);
        return ResponseEntity.ok(new GenericResponse<LancamentoDTO>(lancamentoDTO));
    }

    @PostMapping("/{id}/depara")
    public ResponseEntity<?> salvarTransacaoComoDePara(@PathVariable BigInteger id, @RequestParam String contaMovimento,
        @RequestParam(name = "salvarParaTodos", defaultValue = "true") boolean salvarParaTodos,
            OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(lancamentoService.salvarTransacaoComoDePara(
            id, contaMovimento, salvarParaTodos, authentication));
    }

    @PostMapping("/{id}/outras_contas")
    public ResponseEntity<?> salvarTransacaoComoOutrasContas(@PathVariable BigInteger id,
            @RequestParam String contaMovimento, Principal principal) throws Exception {
        return ResponseEntity.ok(lancamentoService.salvarTransacaoComoOutrasContas(id, contaMovimento, principal));
    }

    @PostMapping("/{id}/ignorar")
    public ResponseEntity<?> salvarTransacaoComoIgnorar(@PathVariable BigInteger id,
                                                        OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(lancamentoService.salvarTransacaoComoIgnorar(id, authentication));
    }

    @GetMapping("/status")
    public ResponseEntity<?> buscaStatusLancementosPorCNPJEmpresa(@RequestParam String cnpjEmpresa, OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(lancamentoService.buscaStatusLancementosPorCNPJEmpresa(cnpjEmpresa, authentication));
    }

    @PostMapping("/regras")
    public ResponseEntity<?> buscarLancamentosPorRegra(@RequestBody List<Regra> regras, @RequestParam String cnpjEmpresa,
                                                       @Valid PageCriteria pageCriteria, Principal principal) throws Exception {
        return ResponseEntity.ok(new GenericPageableResponse<>(lancamentoService.buscarLancamentosPorRegra(
            regras, cnpjEmpresa, pageCriteria, principal
        )));
    }

    @PostMapping("/importar")
    public ResponseEntity<?> importar(@RequestBody ImportacaoLancamentosRequest importacaoLancamentos, OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(lancamentoService.importar(importacaoLancamentos, authentication));
    }
    
    @PostMapping("/arquivo")
    public ResponseEntity<?> salvaArquivo(@RequestBody ArquivoDTO arquivo) {
    	return ResponseEntity.ok(lancamentoService.salvaArquivo(arquivo));
    }
    
    @GetMapping("/porcentagem")
    public ResponseEntity<?> buscaLancamentosPorcentagem(@RequestParam String cnpjEmpresa, @RequestParam String tipoMovimento,
    													 @Valid PageCriteria pageCriteria) throws Exception {
    	return ResponseEntity.ok(lancamentoService.buscaPorcentagem(cnpjEmpresa, tipoMovimento, pageCriteria));
    }
    

}