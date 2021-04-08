package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.commands.lote_processado.ImportacaoLoteProcessado;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LoteProcessadoDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericPageableResponse;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.LoteProcessadoService;

@RestController
@RequestMapping("/api/v1/lote_processado")
public class LoteProcessadoController {

	@Inject
	LoteProcessadoService service;

	@GetMapping
	public ResponseEntity<?> buscaComFiltro(@Valid LoteProcessadoDTO filtro,
										    @Valid PageCriteria criteria) throws Exception {
		return ResponseEntity.ok(new GenericPageableResponse<>(
				service.buscaComFiltro(filtro, criteria)
			));
	}

	@PostMapping
	public ResponseEntity<?> salvaLoteProcessado(@RequestBody LoteProcessadoDTO lote) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.salva(lote)
			));
	}

	 @PostMapping("/importar")
	 public ResponseEntity<?> importarLotesProcessados(@RequestBody ImportacaoLoteProcessado lotes) throws Exception {
		 return ResponseEntity.ok(new GenericResponse<>(
				 service.importaLotes(lotes)
			));
	 }

	 @DeleteMapping("/{id}")
	 public ResponseEntity<?> deletaLoteProcessado(@PathVariable BigInteger id) throws Exception {
		 return ResponseEntity.ok(new GenericResponse<>(
				 service.deletaPorId(id)
			));
	 }

} 
