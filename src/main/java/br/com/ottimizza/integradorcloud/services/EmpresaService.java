package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.dtos.empresa.EmpresaDTO;
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

    //
    //
    //
    public EmpresaDTO buscarEmpresas(EmpresaDTO filter) throws Exception {
        throw new Exception("");
    }

    public EmpresaDTO salvar(EmpresaDTO empresaDTO) throws Exception {
        throw new Exception("");
    }

}