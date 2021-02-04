package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfhistorico.SFHistorico;
import br.com.ottimizza.integradorcloud.domain.dtos.sfparticularidade.SFParticularidade;
import br.com.ottimizza.integradorcloud.domain.dtos.user.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.mappers.historico.HistoricoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.repositories.empresa.EmpresaRepository;
import br.com.ottimizza.integradorcloud.repositories.grupo_regra.GrupoRegraRepository;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;
import br.com.ottimizza.integradorcloud.utils.StringUtils;

@Service
public class SalesForceService {

	@Inject
	SalesForceClient salesForceClient;
	
	@Inject
	GrupoRegraRepository grupoRegraRepository;

	@Inject
	RegraRepository regraRepository;
	
	
	@Inject 
    OAuthClient oauthClient;
	
	public String upsertRegra(BigInteger grupoRegraId, String authorization) throws Exception {
		GrupoRegra grupoRegra = grupoRegraRepository.findById(grupoRegraId).get();
		List<Regra> regras = regraRepository.buscarPorGrupoRegra(grupoRegraId);
		
		int contador = 0;
		int idexRemove = -1;
		
		for(Regra regra : regras) {
			String campo = regra.getCampo();
			if(campo.contains("tipoMovimento")) idexRemove = contador;
			regra.setCampo(StringUtils.trataProSalesForce(campo, regra.getCondicao()));
			contador ++;
		}
		if(idexRemove != -1) regras.remove(idexRemove);
		grupoRegra.setRegras(regras);
		SFParticularidade sfParticularidade = GrupoRegraMapper.toSalesForce(grupoRegra);
		String retorno = salesForceClient.upsert(grupoRegraId, sfParticularidade, authorization).getBody();

		return retorno;
	}
	
	public List<BigInteger> getGrupoRegraIds(GrupoRegraDTO filtro) throws Exception {
		return grupoRegraRepository.findId(filtro.getCnpjEmpresa(), filtro.getTipoLancamento());
	}
	
	public String upsertHistorico(BigInteger historicoId, HistoricoDTO historico, String authorization) throws Exception {
		SFHistorico SFhistorico = HistoricoMapper.toSalesForce(historico);
		return salesForceClient.upsertHistorico(historicoId, SFhistorico, authorization).getBody();
	}
	
	
}
