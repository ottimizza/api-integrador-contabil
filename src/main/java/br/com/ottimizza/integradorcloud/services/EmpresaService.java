package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFProdutoContabilidade;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.EmpresaMapper;
import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.repositories.ContabilidadeRepository;
import br.com.ottimizza.integradorcloud.repositories.EmpresaRepository;
import br.com.ottimizza.integradorcloud.utils.ServiceUtils;

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
    	UserDTO userInfo = oauthClient.getUserInfo(ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
    	OrganizationDTO contabilidadeOauth = oauthClient.buscaContabilidadePorId(userInfo.getOrganization().getId(), ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
    	OrganizationDTO empresaOauth = null;
    	Contabilidade contabilidade = null;
        String produtoContabilidade = "";
    	String empresaOauthString = "";
    	String nomeResumido = empresaDTO.getNomeResumido().trim();
        nomeResumido = nomeResumido.replaceFirst(nomeResumido.substring(0, 1), nomeResumido.substring(0, 1).toUpperCase());

        SFEmpresa empresaCRM = salesForceClient.getEmpresa(nomeResumido, ServiceUtils.getAuthorizationHeader(authentication)).getBody();
        if(empresaCRM != null && !empresaCRM.getCnpj().equals(empresaDTO.getCnpj()))
            throw new IllegalArgumentException("Nome resumido já encontrado, informe outro!");

    	try {
    		empresaOauth = oauthClient.buscaEmpresa(empresaDTO.getCnpj(),userInfo.getOrganization().getId(), 2, ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecords().get(0);
    	} catch(Exception ex) { }
        if(empresaOauth != null ) {
			if(empresaOauth.getCodigoERP() == null || empresaOauth.getCodigoERP().equals("")) {
				empresaOauthString = mapper.writeValueAsString(OrganizationDTO.builder().codigoERP(empresaDTO.getCodigoERP()).build());
        		ServiceUtils.defaultPatch(OAUTH2_SERVER_URL+"/api/v1/organizations/"+empresaOauth.getId(), empresaOauthString, ServiceUtils.getAuthorizationHeader(authentication));
			}
        }
        else {
        	empresaOauth = oauthClient.salvaEmpresa(OrganizationDTO.builder()
        								.cnpj(empresaDTO.getCnpj())
        								.codigoERP(empresaDTO.getCodigoERP())
        								.name(empresaDTO.getRazaoSocial())
        								.organizationId(userInfo.getOrganization().getId())
        								.type(2)
        					.build(), ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
        }
        empresaDTO.setOrganizationId(empresaOauth.getId());
        empresaDTO.setAccountingId(userInfo.getOrganization().getId());
        try {
        	contabilidade = contabilidadeRepository.buscaPorCnpj(contabilidadeOauth.getCnpj());
        } catch(Exception ex) { }
        if(contabilidade == null) {
        	SFContabilidade sfContabilidade = salesForceClient.getContabilidade(contabilidadeOauth.getCnpj(), ServiceUtils.getAuthorizationHeader(authentication)).getBody();
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
            try{
                SFProdutoContabilidade produtoContabilidadeObj = salesForceClient.getProdutoContabilidade(contabilidade.getSalesForceId().substring(0, 15), ServiceUtils.getAuthorizationHeader(authentication)).getBody();
                produtoContabilidade = produtoContabilidadeObj.getIdProduto();
            }
            catch(Exception ex){ }
            SFEmpresa empresaSf = EmpresaMapper.toSalesFoce(empresaDTO).toBuilder()
                    .valorMesIntegracao(60.0)
                    .contailidadeFaturamento(contabilidade.getSalesForceId())
                    .produtoContabilidade(produtoContabilidade)
                    .Previsao_Homologacao(LocalDateTime.now(ZoneId.of("Brazil/East")).plusMonths(1).toString())
                .build();
            salesForceClient.upsertEmpresa(nomeResumido, empresaSf, ServiceUtils.getAuthorizationHeader(authentication));
		return EmpresaMapper.fromEntity(empresa);
    }
    
    public Page<EmpresaDTO> buscarEmpresas(EmpresaDTO filter, PageCriteria pageCriteria, OAuth2Authentication authentication) throws Exception {
        UserDTO userInfo = oauthClient.getUserInfo(ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();

        filter.setAccountingId(userInfo.getOrganization().getId());
       

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
        Example<Empresa> example = Example.of(EmpresaMapper.fromDto(filter), matcher);

        return empresaRepository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize()))
                                .map(EmpresaMapper::fromEntity);
    }

    public EmpresaDTO salvar(EmpresaDTO empresaDTO) throws Exception {
        throw new Exception("");
    }
  
}