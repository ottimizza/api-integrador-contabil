package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.ArquivoS3DTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.client.StorageS3Client;
import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfempresa.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.dtos.user.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.roteiro.RoteiroMapper;
import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;
import br.com.ottimizza.integradorcloud.repositories.ContabilidadeRepository;
import br.com.ottimizza.integradorcloud.repositories.empresa.EmpresaRepository;
import br.com.ottimizza.integradorcloud.repositories.roteiro.RoteiroRepository;

@Service
public class RoteiroService {

	@Inject
	RoteiroRepository repository;
	
	@Inject
	EmpresaRepository empresaRepository;
	
	@Inject
	ContabilidadeRepository contabilidadeRepository;
	
	@Inject
	StorageS3Client s3Client;
	
	@Inject
	OAuthClient oauthClient;
	
	@Inject
	SalesForceClient sfClient;
	
	@Value("${storage-s3.service.url}")
    private String S3_SERVICE_URL;
	
	@Value("${salesforce.service.url}")
    private String SF_SERVICE_URL;
	
	public RoteiroDTO salva(RoteiroDTO roteiroDTO, OAuth2Authentication authentication) throws Exception {
		UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody().getRecord();
		roteiroDTO.setUsuario(userInfo.getUsername());
		
		Roteiro roteiro = RoteiroMapper.fromDTO(roteiroDTO);
		validaRoteiro(roteiro);
		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	public RoteiroDTO uploadPlanilha(BigInteger roteiroId,
									 SalvaArquivoRequest salvaArquivo,
									 MultipartFile arquivo,
									 String authorization) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo.getCnpjEmpresa(), salvaArquivo.getCnpjContabilidade(), salvaArquivo.getApplicationId(), arquivo, authorization).getBody();
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		roteiro = roteiro.toBuilder().status((short) 3).urlArquivo(arquivoS3.getId().toString()).build();
		validaRoteiro(roteiro);
		Empresa empresa = empresaRepository.buscarPorId(roteiro.getEmpresaId()).orElseThrow(() -> new NoResultException("Empresa nao encontrada!"));
		
		Contabilidade contabilidade = contabilidadeRepository.buscaPorCnpj(roteiro.getCnpjContabilidade());
		SFEmpresa empresaCrm = SFEmpresa.builder()
				.Arquivo_Portal(S3_SERVICE_URL+"/api/v1/arquivos/"+arquivoS3.getId().toString()+"/download")
				.Contabilidade_Id(contabilidade.getSalesForceId())
			.build();
		String empresaCrmString = mapper.writeValueAsString(empresaCrm);
		defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+empresa.getNomeResumido(), empresaCrmString, authorization);
		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	public RoteiroDTO patch(BigInteger roteiroId, RoteiroDTO roteiroDTO, OAuth2Authentication authentication) throws Exception {
		UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody().getRecord();
		roteiroDTO.setUsuario(userInfo.getUsername());
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		if(roteiroDTO.getNome() != null && !roteiroDTO.getNome().equals("")) {
			if(repository.buscaPorNomeEmpresaIdTipo(roteiroDTO.getNome(), roteiro.getEmpresaId(), roteiro.getTipoRoteiro()) > 0)
				throw new IllegalArgumentException("Nome de roteiro já existente nesta empresa para este tipo de roteiro!");
		}
		if(roteiroDTO.getTipoRoteiro() != null && !roteiroDTO.getTipoRoteiro().equals("")) {
			if(roteiroDTO.getTipoRoteiro().contains("PAG"))
				roteiroDTO.setTipoProjeto((short) 1);
			else
				roteiroDTO.setTipoProjeto((short) 2);
			
		}
		Roteiro retorno = roteiroDTO.patch(roteiro);
		validaRoteiro(retorno);
		return RoteiroMapper.fromEntity(repository.save(retorno));
	}
	
	public Page<Roteiro> busca(RoteiroDTO filtro, PageCriteria criteria) throws Exception {
		return repository.buscaRoteiros(filtro, criteria);
	}
	
	public String deleta(BigInteger roteiroId) throws Exception {
		repository.deleteById(roteiroId);
		return "Roteiro removido com sucesso!";
	}

	private String getAuthorizationHeader(OAuth2Authentication authentication) {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();
        return MessageFormat.format("Bearer {0}", accessToken);
    }
	
	private String defaultPatch(String url, String body, String authentication) {
    	RestTemplate template = new RestTemplate();
    	
    	HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    	requestFactory.setConnectTimeout(15000);
    	requestFactory.setReadTimeout(15000);
    	
    	template.setRequestFactory(requestFactory);
    	
    	HttpHeaders headers =  new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("Authorization", authentication);
    	
    	return template.patchForObject(url, new HttpEntity<String>(body, headers), String.class);
    }
	
	private boolean validaRoteiro(Roteiro roteiro) throws Exception {
		if(roteiro.getStatus() == 1) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
		}
		if(roteiro.getStatus() == 3) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
		}
		if(roteiro.getStatus() == 5) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
			if(roteiro.getTipoRoteiro() == null || roteiro.getTipoRoteiro().equals(""))
				throw new IllegalArgumentException("Informe o tipo do roteiro!");
			if(roteiro.getTipoProjeto() == null || roteiro.getTipoProjeto().equals(""))
				throw new IllegalArgumentException("Tipo projeto não encontrado para este roteiro!");
		}
		if(roteiro.getStatus() == 6) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
			if(roteiro.getTipoRoteiro() == null || roteiro.getTipoRoteiro().equals(""))
				throw new IllegalArgumentException("Informe o tipo do roteiro!");
			if(roteiro.getTipoProjeto() == null || roteiro.getTipoProjeto().equals(""))
				throw new IllegalArgumentException("Tipo projeto não encontrado para este roteiro!");
			if(roteiro.getChecklist() == null)
				throw new IllegalArgumentException("Checklist não encontrada para este roteiro!"); 
		}
		if(roteiro.getStatus() == 7) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
			if(roteiro.getTipoRoteiro() == null || roteiro.getTipoRoteiro().equals(""))
				throw new IllegalArgumentException("Informe o tipo do roteiro!");
			if(roteiro.getTipoProjeto() == null || roteiro.getTipoProjeto().equals(""))
				throw new IllegalArgumentException("Tipo projeto não encontrado para este roteiro!");
			if(roteiro.getChecklist() == null)
				throw new IllegalArgumentException("Checklist não encontrada para este roteiro!"); 
			if(roteiro.getNome() == null || roteiro.getNome().equals(""))
				throw new IllegalArgumentException("Informe o nome para o seu roteiro!"); 
		}
		return true;
	}
}
