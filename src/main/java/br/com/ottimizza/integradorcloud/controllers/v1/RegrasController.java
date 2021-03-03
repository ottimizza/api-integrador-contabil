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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.GrupoRegraIgnoradaDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.RegraService;

@RestController
@RequestMapping("/api/v1/regras")
public class RegrasController {

    @Inject
    RegraService regraService;

    @GetMapping
    public ResponseEntity<?> buscarRegras(@Valid GrupoRegraDTO filter, 
                                          @Valid PageCriteria pageCriteria, 
                                          OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericPageableResponse<GrupoRegraDTO>(
            regraService.buscarRegras(filter, pageCriteria, authentication)
        ));
    }

    @PostMapping
    public ResponseEntity<?> criarRegra(@RequestBody GrupoRegraDTO grupoRegraDTO,
    									@Valid Short sugerir,
    									@Valid String regraSugerida,
                                        OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
            regraService.salvar(grupoRegraDTO, sugerir, regraSugerida,  authentication)
        ));
    }
    
    @PostMapping("{id}")
    public ResponseEntity<?> clonarRegra(@PathVariable("id")BigInteger id,
    									 OAuth2Authentication authentication) throws Exception {
    	return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
    		regraService.clonar(id, authentication)
    	));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarRegra(@PathVariable BigInteger id, 
                                            @RequestBody GrupoRegraDTO grupoRegraDTO, 
                                            OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
            regraService.atualizar(id, grupoRegraDTO, authentication)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarRegra(@PathVariable BigInteger id,  
                                         OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
            regraService.apagar(id, authentication)
        ));
    }

    @PutMapping("/{id}/alterar_posicao")
    public ResponseEntity<?> alterarPosicao(@PathVariable BigInteger id, 
                                            @RequestBody GrupoRegraDTO grupoRegraDTO, 
                                            OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
            regraService.alterarPosicao(id, grupoRegraDTO.getPosicao(), authentication)
        ));                                  
    }

    @PutMapping("/{id}/posicao")
    public ResponseEntity<?> moverRegra(@PathVariable BigInteger id, 
                                            @RequestBody GrupoRegraDTO grupoRegraDTO, 
                                            OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
            regraService.alterarPosicao(id, grupoRegraDTO.getPosicao(), authentication)
        ));                                  
    }

    @PutMapping("/{id}/posicao/inicio")
    public ResponseEntity<?> moverRegraParaInicio(@PathVariable BigInteger id, 
                                                  OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
            regraService.moverRegraParaInicio(id, authentication)
        ));                                  
    }

    @PutMapping("/{id}/posicao/final")
    public ResponseEntity<?> moverRegraParaFinal(@PathVariable BigInteger id, 
                                                 OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
            regraService.moverRegraParaFinal(id, authentication)
        ));                                  
    }
    
    @GetMapping("/sugerir/{lancamentoId}")
    public ResponseEntity<?> sugerirRegra(@Valid String cnpjContabilidade,
    									  @Valid Short busca,
    									  @PathVariable("lancamentoId") BigInteger lancamentoId) throws Exception {
    	return ResponseEntity.ok(new GenericResponse<GrupoRegraDTO>(
    		regraService.sugerirRegra(cnpjContabilidade, busca, lancamentoId)
    	));
    }
    
    @PostMapping("/sugerir/ignorar")
    public ResponseEntity<?> ignorarRegraSugerida(@RequestBody GrupoRegraIgnoradaDTO grupoRegra,
    											  OAuth2Authentication authentication) throws Exception {
    	return ResponseEntity.ok(new GenericResponse<GrupoRegraIgnoradaDTO>(
    			regraService.ignorarSugestaoRegra(grupoRegra, authentication)
    		));
    }

}
