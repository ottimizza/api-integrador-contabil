package br.com.ottimizza.integradorcloud.controllers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.HttpEntity;
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

import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfparticularidade.SFParticularidade;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;
import br.com.ottimizza.integradorcloud.services.RegraService;

@RestController
@RequestMapping("/api/test")
public class COntrollerTest {

	@Inject 
	SalesForceClient salesForceClient;
	
	@Inject
	GrupoRegraRepository grupoRegraRepository;
	
	@Inject
	RegraRepository regraRepository;
	
	@Inject
	RegraService regraService;
	
	@PatchMapping("/patch/{id}")
	public ResponseEntity<String> patch(@PathVariable BigInteger id, @RequestHeader("Authorization") String authorization) throws Exception {
		GrupoRegra grupoRegra = grupoRegraRepository.findById(id).get();
		grupoRegra.setRegras(regraRepository.buscarPorGrupoRegra(id));
		
		SFParticularidade sfParticularidade = GrupoRegraMapper.toSalesForce(grupoRegra);
		System.out.println("id: " + id);
		System.out.println("sfpart: " + sfParticularidade.toString());
		System.out.println("auth: " + authorization);
		return salesForceClient.upsert(id, sfParticularidade, authorization);
	}
	
	@GetMapping("/test")
	public List<GrupoRegra> get(@Valid GrupoRegraDTO filter, OAuth2Authentication authentication) throws Exception{
		return regraService.findToSalesForce(filter, authentication);
	}
	
	@PostMapping("/postList")
	public ResponseEntity<String> post(@Valid GrupoRegraDTO filter, @RequestHeader("Authorization") String authorization, OAuth2Authentication authentication) throws Exception {
		List<GrupoRegra> listaGrupoRegras = regraService.findToSalesForce(filter, authentication);
		int i = 1;
		for(GrupoRegra grupoRegra : listaGrupoRegras) {
			BigInteger grupoRegraId = grupoRegra.getId();
			grupoRegra.setRegras(regraRepository.buscarPorGrupoRegra(grupoRegraId));
			SFParticularidade particularidade =  GrupoRegraMapper.toSalesForce(grupoRegra);
			
			System.out.println("Montando Particularidade "+ i + " " + particularidade.toString());
			i ++;
			
			salesForceClient.upsert(grupoRegraId, particularidade, authorization);
		}
		return ResponseEntity.ok("Deu certo");
	}
	
	
}
