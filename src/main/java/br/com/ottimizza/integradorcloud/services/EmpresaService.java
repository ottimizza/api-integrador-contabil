package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.empresa.EmpresaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.user.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.EmpresaMapper;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.repositories.empresa.EmpresaRepository;

@Service // @formatter:off
public class EmpresaService {

    @Inject
    EmpresaRepository empresaRepository;

    @Inject
    OAuthClient oauthClient;

    public Empresa buscarPorId(BigInteger id) throws Exception {
        return empresaRepository.buscarPorId(id).orElseThrow(() -> new NoResultException());
    }

    public Empresa buscarPorOrganizationId(BigInteger organizationId) throws Exception {
        return empresaRepository.buscarPorOrganizationId(organizationId).orElseThrow(() -> new NoResultException());
    }

    public Empresa buscarPorCNPJ(String cnpj) throws Exception {
        return empresaRepository.buscarPorCNPJ(cnpj).orElseThrow(() -> new NoResultException());
    }

    public Empresa salvar(Empresa empresaDTO) throws Exception {
        // Busca dados por CNPJ do Servidor de OAuth.
        // oauthClient.buscarEmpresasPorCNPJ(empresaDTO.getCnpj(), authorization)
        throw new Exception("");
    }

    public Page<EmpresaDTO> buscarEmpresas(EmpresaDTO filter, PageCriteria pageCriteria, OAuth2Authentication authentication) throws Exception {
        UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody();
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

}