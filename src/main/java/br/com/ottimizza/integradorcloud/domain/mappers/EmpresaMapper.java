package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.empresa.EmpresaDTO;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;

public class EmpresaMapper { // @formatter:off

    public static Empresa fromDto(EmpresaDTO empresaDTO) {
        return Empresa.builder()
                .id(empresaDTO.getId())
                .razaoSocial(empresaDTO.getRazaoSocial())
                .codigoERP(empresaDTO.getCodigoERP())
                .cnpj(empresaDTO.getCnpj())
                .organizationId(empresaDTO.getOrganizationId())
                .accountingId(empresaDTO.getAccountingId())
            .build(); 
    }

    public static EmpresaDTO fromEntity(Empresa empresa) {
        return EmpresaDTO.builder()
                .id(empresa.getId())
                .razaoSocial(empresa.getRazaoSocial())
                .codigoERP(empresa.getCodigoERP())
                .cnpj(empresa.getCnpj())
                .organizationId(empresa.getOrganizationId())
                .accountingId(empresa.getAccountingId())
            .build(); 
    }

    public static List<EmpresaDTO> fromEntities(List<Empresa> empresas) {
        return empresas.stream().map(EmpresaMapper::fromEntity).collect(Collectors.toList());
    }

}