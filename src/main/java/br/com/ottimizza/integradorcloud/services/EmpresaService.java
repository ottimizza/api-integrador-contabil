package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.EmpresaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.OrganizationDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFContabilidade;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.EmpresaMapper;
import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.repositories.ContabilidadeRepository;
import br.com.ottimizza.integradorcloud.repositories.EmpresaRepository;

@Service // @formatter:off
public class EmpresaService {

    @Inject
    EmpresaRepository empresaRepository;

    @Inject
    ContabilidadeRepository contabilidadeRepository;
    
    @Inject
    OAuthClient oauthClient;

    @Inject
    SalesForceClient salesForceClient;
    
    @Value("${oauth.service.url}")
    private String OAUTH2_SERVER_URL;
    
    public Empresa buscarPorId(BigInteger id) throws Exception {
        return empresaRepository.buscarPorId(id).orElseThrow(() -> new NoResultException());
    }

    public Empresa buscarPorOrganizationId(BigInteger organizationId) throws Exception {
        return empresaRepository.buscarPorOrganizationId(organizationId).orElseThrow(() -> new NoResultException());
    }

    public Empresa buscarPorCNPJ(String cnpj) throws Exception {
        return empresaRepository.buscarPorCNPJ(cnpj).orElseThrow(() -> new NoResultException());
    }

    public EmpresaDTO salvar(EmpresaDTO empresaDTO, OAuth2Authentication authentication) throws Exception {
    	ObjectMapper mapper = new ObjectMapper();
    	UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody().getRecord();
    	OrganizationDTO contabilidadeOauth = oauthClient.buscaContabilidadePorId(userInfo.getOrganization().getId(), getAuthorizationHeader(authentication)).getBody().getRecord();
    	OrganizationDTO empresaOauth = null;
    	Contabilidade contabilidade = null;
    	String empresaOauthString = "";
    	String nomeResumido = empresaDTO.getNomeResumido().trim();
        nomeResumido = nomeResumido.replaceFirst(nomeResumido.substring(0, 1), nomeResumido.substring(0, 1).toUpperCase());
    	try {
    		empresaOauth = oauthClient.buscaEmpresa(empresaDTO.getCnpj(),userInfo.getOrganization().getId(), 2, getAuthorizationHeader(authentication)).getBody().getRecords().get(0);
    	} catch(Exception ex) { }
        if(empresaOauth != null ) {
			if(empresaOauth.getCodigoERP().equals("") || empresaOauth.getCodigoERP().equals(null)) {
				empresaOauthString = mapper.writeValueAsString(OrganizationDTO.builder().codigoERP(empresaDTO.getCodigoERP()).build());
        		defaultPatch(OAUTH2_SERVER_URL+"/api/v1/organizations/"+empresaOauth.getId(), empresaOauthString, getAuthorizationHeader(authentication));
			}
        }
        else {
        	empresaOauth = oauthClient.salvaEmpresa(OrganizationDTO.builder()
        								.cnpj(empresaDTO.getCnpj())
        								.codigoERP(empresaDTO.getCodigoERP())
        								.name(empresaDTO.getRazaoSocial())
        								.organizationId(userInfo.getOrganization().getId())
        								.type(2)
        					.build(), getAuthorizationHeader(authentication)).getBody().getRecord();
        }
        empresaDTO.setOrganizationId(empresaOauth.getId());
        empresaDTO.setAccountingId(userInfo.getOrganization().getId());
        try {
        	contabilidade = contabilidadeRepository.buscaPorCnpj(contabilidadeOauth.getCnpj());
        } catch(Exception ex) { }
        if(contabilidade == null) {
        	SFContabilidade sfContabilidade = salesForceClient.getContabilidade(contabilidadeOauth.getCnpj(), getAuthorizationHeader(authentication)).getBody();
        	contabilidade = contabilidadeRepository.save(Contabilidade.builder()
        				.cnpj(contabilidadeOauth.getCnpj())
        				.nome(contabilidadeOauth.getName())
        				.ouathId(contabilidadeOauth.getId())
        				.salesForceId(sfContabilidade.getId())
        			.build());
        }
        empresaDTO.setContabilidadeCrmId(contabilidade.getSalesForceId());
        Empresa empresa = empresaRepository.save(EmpresaMapper.fromDto(empresaDTO));
        if(empresa.getRazaoSocial() != null && !empresa.getRazaoSocial().equals(""))
        	empresaDTO.setRazaoSocial(empresaDTO.getRazaoSocial().toUpperCase());
        SFEmpresa empresaSf = EmpresaMapper.toSalesFoce(empresaDTO);
        salesForceClient.upsertEmpresa(nomeResumido, empresaSf, getAuthorizationHeader(authentication));
		return EmpresaMapper.fromEntity(empresa);
    }
    
    public Page<EmpresaDTO> buscarEmpresas(EmpresaDTO filter, PageCriteria pageCriteria, OAuth2Authentication authentication) throws Exception {
        UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody().getRecord();

        filter.setAccountingId(userInfo.getOrganization().getId());
       

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<Empresa> example = Example.of(EmpresaMapper.fromDto(filter), matcher);

        return empresaRepository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize()))
                                .map(EmpresaMapper::fromEntity);
    }

    public EmpresaDTO salvar(EmpresaDTO empresaDTO) throws Exception {
        throw new Exception("");
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
  

}