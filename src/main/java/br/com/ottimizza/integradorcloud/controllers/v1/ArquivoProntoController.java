package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.commands.arquivo_pronto.ImportacaoArquivoPronto;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.ArquivoProntoDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.ArquivoProntoService;

@RestController
@RequestMapping("/api/v1/arquivo_pronto")
public class ArquivoProntoController {

	@Inject
	ArquivoProntoService service;

	@GetMapping
	public ResponseEntity<?> buscaComFiltro(@Valid ArquivoProntoDTO arquivo,
											@Valid PageCriteria criteria) throws Exception {
		return ResponseEntity.ok(new GenericPageableResponse<>(
				service.buscaComFiltro(arquivo, criteria)
			));
	}

	@PostMapping
	public ResponseEntity<?> salvaArquivoPronto(@RequestBody ArquivoProntoDTO arquivo) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.salvaArquivo(arquivo)
			));
	}

	@PostMapping("/importar")
	public ResponseEntity<?> importarArquivosProntos(@RequestBody ImportacaoArquivoPronto arquivos) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.importarArquivos(arquivos)
			));
	}

	@PutMapping("{id}")
	public ResponseEntity<?> atualizaArquivoPronto(@PathVariable BigInteger id,
												   @RequestBody ArquivoProntoDTO arquivo) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.patchArquivo(id, arquivo)
			));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deletaArquivoPronto(@PathVariable BigInteger id) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.deletaArquivo(id)
			));
	}


}