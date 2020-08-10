package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.empresa.EmpresaDTO;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;

/** @formatter:off
 * 
 * Classe responsável pelo mapeamento de Entidade para DTO e vice-versa.
 */
public class EmpresaMapper { 

    /**
     * Função para conversão de DTO para Entidade
     * 
     * @param empresaDTO | Objeto DTO para conversão
     * @return Entidade Empresa para uso em consultas de banco de dados.
     */
    public static Empresa fromDto(EmpresaDTO empresaDTO) {
        return Empresa.builder()
                .id(empresaDTO.getId())
                .razaoSocial(empresaDTO.getRazaoSocial())
                .codigoERP(empresaDTO.getCodigoERP())
                .cnpj(empresaDTO.getCnpj())
                .nomeCompleto(empresaDTO.getNomeCompleto())
                .organizationId(empresaDTO.getOrganizationId())
                .accountingId(empresaDTO.getAccountingId())
            .build(); 
    }

    /**
     * Função para conversão de Entidade para DTO
     * 
     * @param empresa | Entidade para conversão
     * @return DTO Empresa para uso em retorno de Controller REST.
     */
    public static EmpresaDTO fromEntity(Empresa empresa) {
        return EmpresaDTO.builder()
                .id(empresa.getId())
                .razaoSocial(empresa.getRazaoSocial())
                .codigoERP(empresa.getCodigoERP())
                .cnpj(empresa.getCnpj())
                .nomeCompleto(empresa.getNomeCompleto())
                .organizationId(empresa.getOrganizationId())
                .accountingId(empresa.getAccountingId())
            .build(); 
    }

    public static List<EmpresaDTO> fromEntities(List<Empresa> empresas) {
        return empresas.stream().map(EmpresaMapper::fromEntity).collect(Collectors.toList());
    }

}