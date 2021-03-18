package br.com.ottimizza.integradorcloud.services;


import java.math.BigInteger;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.CheckListRespostasDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.mappers.CheckListMapper;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckList;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListPerguntas;
import br.com.ottimizza.integradorcloud.domain.models.checklist.CheckListRespostas;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListPerguntasRepository;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListRespostasRepository;
import br.com.ottimizza.integradorcloud.utils.ServiceUtils;

@Service
public class CheckListService {
	
	@Inject
	CheckListPerguntasRepository perguntasRepository;
	
	@Inject
	CheckListRespostasRepository respostasRepository;
	
	@Inject
	OAuthClient oauthClient;

	@Value("${salesforce.service.url}")
    private String SF_SERVICE_URL;

	public CheckList buscaCheckList(String tipoRoteiro) throws Exception {
		CheckList checkList = new CheckList();
		checkList.setPerguntas(perguntasRepository.buscaPorTipo(tipoRoteiro));
		checkList.setObservacoes(perguntasRepository.buscaObservacoes());
		return checkList;
	}
	
	// PERGUNTAS 
	
	public CheckListPerguntas salvaPerguntas(CheckListPerguntas pergunta) throws Exception {
		return perguntasRepository.save(pergunta);
	}
	
	// RESPOSTAS
	
	public CheckListRespostasDTO salvaResposta(CheckListRespostasDTO respostaDTO, String authorization, OAuth2Authentication authentication) throws Exception {
		CheckListRespostas respostaDb = null;
		
		//List<BigInteger> idsPerguntasContato = perguntasRepository.getIdsPerguntasContanto();
		try {
			respostaDb = respostasRepository.checkResposta(respostaDTO.getRoteiroId(), respostaDTO.getPerguntaId());
		}catch(Exception ex) { } 
		if(respostaDb != null) {
			CheckListRespostas resposta = respostasRepository.save(respostaDTO.patch(respostaDb));
			atualizaEmpresaCRM(respostaDTO, authorization, authentication);
			return CheckListMapper.respostasFromEntity(resposta);
		}
		else {
			//if(idsPerguntasContato.contains(respostaDTO.getPerguntaId())) {
			CheckListRespostas resposta = respostasRepository.save(CheckListMapper.respostasFromDTO(respostaDTO));
			atualizaEmpresaCRM(respostaDTO, authorization, authentication);
			return CheckListMapper.respostasFromEntity(resposta);
		}
	}
	
	public Page<CheckListRespostasDTO> getRespostas(CheckListRespostasDTO filtro, PageCriteria pageCriteria) throws Exception {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<CheckListRespostas> example = Example.of(CheckListMapper.respostasFromDTO(filtro), matcher);
		
        return respostasRepository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize()))
        										.map(CheckListMapper::respostasFromEntity);
	}
	
	public void atualizaEmpresaCRM(CheckListRespostasDTO respostaDTO, String authorization, OAuth2Authentication authentication) throws Exception {
		UserDTO user = oauthClient.getUserInfo(authorization).getBody().getRecord();
		SFEmpresa empresaCrm = null;
		ObjectMapper mapper = new ObjectMapper();
		if(perguntasRepository.getDescricaoPorId(respostaDTO.getPerguntaId()).contains("1.2")) {
			String nomeResumido = perguntasRepository.getNomeEmpresaPorRoteiroId(respostaDTO.getRoteiroId());
			empresaCrm = SFEmpresa.builder()
					.ERP_do_cliente(respostaDTO.getResposta())
					.build();
			String empresaCrmString = mapper.writeValueAsString(empresaCrm);
			ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+nomeResumido, empresaCrmString,authorization);
		}
		else if(perguntasRepository.getDescricaoPorId(respostaDTO.getPerguntaId()).contains("1.3")) {
			String nomeResumido = perguntasRepository.getNomeEmpresaPorRoteiroId(respostaDTO.getRoteiroId());
			empresaCrm = SFEmpresa.builder()
					.Horas_para_digitar(respostaDTO.getResposta())
					.build();
			String empresaCrmString = mapper.writeValueAsString(empresaCrm);
			ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+nomeResumido, empresaCrmString,authorization);
		}
		else if(perguntasRepository.getDescricaoPorId(respostaDTO.getPerguntaId()).contains("1.5")) {
			String nomeResumido = perguntasRepository.getNomeEmpresaPorRoteiroId(respostaDTO.getRoteiroId());
			empresaCrm = SFEmpresa.builder()
					.Nome_de_quem_faz_o_fechamento(respostaDTO.getResposta())
					.Envolvidos(user.getFirstName()+" / "+respostaDTO.getResposta())
					.build();
			String empresaCrmString = mapper.writeValueAsString(empresaCrm);
			ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+nomeResumido, empresaCrmString,authorization);
		}
		else if(perguntasRepository.getDescricaoPorId(respostaDTO.getPerguntaId()).contains("1.6")) {
			String nomeResumido = perguntasRepository.getNomeEmpresaPorRoteiroId(respostaDTO.getRoteiroId());
			empresaCrm = SFEmpresa.builder()
					.Email_de_quem_faz_o_fechamento(respostaDTO.getResposta())
				.build();
			String empresaCrmString = mapper.writeValueAsString(empresaCrm);
			ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+nomeResumido, empresaCrmString, authorization);
		}
		else if(perguntasRepository.getDescricaoPorId(respostaDTO.getPerguntaId()).contains("1.7")) {
			String nomeResumido = perguntasRepository.getNomeEmpresaPorRoteiroId(respostaDTO.getRoteiroId());
			empresaCrm = SFEmpresa.builder()
					.Email_Gestor(respostaDTO.getResposta())
				.build();
			String empresaCrmString = mapper.writeValueAsString(empresaCrm);
			ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+nomeResumido, empresaCrmString, authorization);
		}
	}
	
}
