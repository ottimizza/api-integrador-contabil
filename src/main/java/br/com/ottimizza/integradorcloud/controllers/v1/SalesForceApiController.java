package br.com.ottimizza.integradorcloud.controllers.v1;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfhistorico.SFHistorico;
import br.com.ottimizza.integradorcloud.domain.dtos.sfparticularidade.SFParticularidade;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.mappers.historico.HistoricoMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;
import br.com.ottimizza.integradorcloud.services.RegraService;
import br.com.ottimizza.integradorcloud.utils.StringUtils;

@RestController
@RequestMapping("/api/v1/salesforce")
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
		int idexRemove = -1;
		
		for(Regra regra : regras) {
			String campo = regra.getCampo();
			if(campo.contains("tipoMovimento")) idexRemove = contador;
			regra.setCampo(StringUtils.trataProSalesForce(campo));
			contador ++;
		}
		if(idexRemove != -1) regras.remove(idexRemove);
		grupoRegra.setRegras(regras);
		SFParticularidade sfParticularidade = GrupoRegraMapper.toSalesForce(grupoRegra);
		return salesForceClient.upsert(id, sfParticularidade, authorization);
	}
	
	@GetMapping("/id")
	public List<BigInteger> getId(@Valid GrupoRegraDTO filter){
		return grupoRegraRepository.findId(filter.getCnpjEmpresa(), filter.getTipoLancamento());
	}

	@PatchMapping("/historico/{id}")
	public ResponseEntity<?> patchHistorico(@PathVariable BigInteger id, 
											@RequestBody HistoricoDTO historico, 
											@RequestHeader("Authorization") String authorization) throws Exception {
		
		SFHistorico SFhistorico = HistoricoMapper.toSalesForce(historico);
		return salesForceClient.upsertHistorico(id, SFhistorico, authorization);
	}

}
