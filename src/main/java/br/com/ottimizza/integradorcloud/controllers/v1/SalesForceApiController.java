package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import org.h2.util.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfparticularidade.SFParticularidade;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.domain.responses.GenericResponse;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;
import br.com.ottimizza.integradorcloud.services.RegraService;
import br.com.ottimizza.integradorcloud.utils.StringUtils;

@RestController
@RequestMapping("/api/sf")
public class SalesForceApiController {

	@Inject
	SalesForceClient salesForceClient;

	@Inject
	GrupoRegraRepository grupoRegraRepository;

	@Inject
	RegraRepository regraRepository;

	@Inject
	RegraService regraService;

	@PatchMapping("/patch/{id}")
	public ResponseEntity<String> patch(@PathVariable BigInteger id,
			@RequestHeader("Authorization") String authorization) throws Exception {
		GrupoRegra grupoRegra = grupoRegraRepository.findById(id).get();
		List<Regra> regras = regraRepository.buscarPorGrupoRegra(id);
		int contador = 0;
		while(contador <= regras.size()) {
			String campo = regras.get(contador).getCampo();
			if(campo.contains("tipoMovimento")) regras.remove(contador);
			regras.get(contador).setCampo(StringUtils.trataProSalesForce(campo));
			if(contador > regras.size()) break;
			contador ++;
		}
		grupoRegra.setRegras(regras);
		SFParticularidade sfParticularidade = GrupoRegraMapper.toSalesForce(grupoRegra);
		return salesForceClient.upsert(id, sfParticularidade, authorization);
	}
	
	@GetMapping("/id")
	public List<BigInteger> getId(@Valid GrupoRegraDTO filter){
		return grupoRegraRepository.findId(filter.getCnpjEmpresa(), filter.getTipoLancamento());
	}

	@PostMapping("/importar")
	public ResponseEntity<?> post(@Valid GrupoRegraDTO filter, @RequestHeader("Authorization") String authorization,
			OAuth2Authentication authentication) throws Exception {
		if (filter.getCnpjEmpresa() == null || filter.getCnpjEmpresa() == "") {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cnpj da empresa obrigatório para o envio!");
		}
		List<GrupoRegra> listaGrupoRegras = regraService.findToSalesForce(filter, authentication);
		
		for (GrupoRegra grupoRegra : listaGrupoRegras) {

			List<Regra> regras = regraRepository.buscarPorGrupoRegra(grupoRegra.getId());
			SFParticularidade particularidade = GrupoRegraMapper.toSalesForce(grupoRegra, regras, false);
			salesForceClient.upsert(grupoRegra.getId(), particularidade, authorization);
		}
		GenericResponse<GrupoRegra> resposta = new GenericResponse<GrupoRegra>();
		resposta.setMessage("Enviado regras com sucesso!");
		resposta.setStatus("success");

		return ResponseEntity.ok(resposta);
	}

}
