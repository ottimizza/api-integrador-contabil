package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LivroCaixaService;

@RestController
@RequestMapping("/api/v1/livro_caixa")
public class LivroCaixaController {
	
	@Inject
	LivroCaixaService service;
	
	@GetMapping
	public ResponseEntity<?> buscaComFiltro(@Valid LivroCaixaDTO filtro,
										 	@Valid PageCriteria criteria) throws Exception {
		return ResponseEntity.ok(new GenericPageableResponse<LivroCaixa>(
				service.buscaComFiltro(filtro, criteria)
			));
	}
	
	@PostMapping
	public ResponseEntity<?> salvaLivroCaixa(@RequestBody LivroCaixaDTO livroCaixa) throws Exception {
		return ResponseEntity.ok(new GenericResponse<LivroCaixaDTO>(
				service.salva(livroCaixa)
			));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizaLivroCaixa(@PathVariable BigInteger id,
												@RequestBody LivroCaixaDTO livroCaixa) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.patch(id, livroCaixa)
			));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletaLivroCaixa(@PathVariable BigInteger id) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.deletaPorId(id)
			));
	}
	
	@GetMapping("/sugerir/{id}")
	public ResponseEntity<?> sugerirRegra(@PathVariable("id") BigInteger livroCaixaId,
										  @Valid String cnpjContabilidade,
										  @Valid String cnpjEmpresa ) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.sugerirRegra(livroCaixaId, cnpjContabilidade, cnpjEmpresa)
			));
	}
	
	@DeleteMapping("/nao_integrado/{id}")
	public ResponseEntity<?> deletaNaoIntegradoLC(@PathVariable BigInteger id) throws Exception {
		JSONObject response = service.deletaNaoIntegrado(id);
		
		if (response.get("status") == "Unauthorized") return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		return (response.get("status") == "Success") ? ResponseEntity.ok(response.toString()) : ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/clonar/{id}")
	public ResponseEntity<?> clonarLivroCaixa(@PathVariable BigInteger id, OAuth2Authentication authentication) {
		LivroCaixaDTO response = service.clonarLivroCaixa(id, authentication);
		return (response != null) ? ResponseEntity.ok(new GenericResponse<LivroCaixaDTO>(response)) : ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/ultimo_lanc")
	public ResponseEntity<LivroCaixaDTO> buscaUltimoLancamento(@Valid String cnpjContabilidade,
			  													@Valid String cnpjEmpresa) throws Exception {
		return ResponseEntity.ok(service.buscaUltimoLancamentoContabilidadeEmpresa(cnpjContabilidade, cnpjEmpresa));
		
	}
	
	@PostMapping("/{idLivroCaixa}")
	ResponseEntity<?> uploadImagem(@PathVariable("idLivroCaixa") BigInteger idLivroCaixa,
									 @Valid SalvaArquivoRequest salvaArquivo,
									 @RequestParam("file") MultipartFile arquivo,
									 @RequestHeader("Authorization") String authorization) throws Exception {

		return ResponseEntity.ok(new GenericResponse<>(
				service.uploadFile(idLivroCaixa, salvaArquivo, arquivo, authorization)
				));
	}
}
