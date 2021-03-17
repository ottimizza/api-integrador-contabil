package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.domain.dtos.CategoriaDTO;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.services.CategoriaService;

@RestController
@RequestMapping("api/v1/categorias")
public class CategoriaController {

	@Inject
	CategoriaService service;

	@PostMapping
	public ResponseEntity<?> salva(@RequestBody CategoriaDTO categoria) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.salva(categoria)
			));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<?> patch(@PathVariable BigInteger id,
								   @RequestBody CategoriaDTO categoria) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.patch(id, categoria)
			));
	}

	@GetMapping
	public ResponseEntity<?> buscaPorDescricao(@Valid CategoriaDTO categoria) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.buscaPorDescricao(categoria.getDescricao())
			));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletaPorId(@PathVariable BigInteger id) throws Exception {
		return ResponseEntity.ok(new GenericResponse<>(
				service.deletaPorId(id)
			));
	}


}