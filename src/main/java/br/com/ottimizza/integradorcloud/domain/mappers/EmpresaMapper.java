package br.com.ottimizza.integradorcloud.domain.mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.empresa.EmpresaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfempresa.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.dtos.sfempresa.SFEmpresa.SFEmpresaBuilder;
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
                .nomeResumido(empresaDTO.getNomeResumido())
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
                .nomeResumido(empresa.getNomeResumido())
                .organizationId(empresa.getOrganizationId())
                .accountingId(empresa.getAccountingId())
            .build(); 
    }

    public static List<EmpresaDTO> fromEntities(List<Empresa> empresas) {
        return empresas.stream().map(EmpresaMapper::fromEntity).collect(Collectors.toList());
    }
    
    public static SFEmpresa toSalesFoce(EmpresaDTO empresa) {
    	int dia = LocalDate.now().getDayOfMonth(); int mes = LocalDate.now().getMonthValue(); int ano = LocalDate.now().getYear();
    	
    	return SFEmpresa.builder()
    			.Possui_OIC("Possui OIC")
    			.Resumo_Prox_Passo("Ativar OIC - Entraremos em contato para termos mais informacoes do projeto.")
    			.Status_Projeto("01. Empresa Listada")
    			.Codigo_Empresa_ERP(empresa.getCodigoERP())
    			.Status_Report_Data(dia +"/"+mes+"/"+ano)
    			.Contabilidade_Id(empresa.getContabilidadeCrmId())
    			.Nome_Empresa(empresa.getNomeCompleto())
    			.O_Que_Foi_Feito_Hoje("Novo cliente OIC")
    			.Cnpj(empresa.getCnpj())
    			.Nome_Resumido(empresa.getNomeResumido())
    			.Envolvidos("Kleber")
    			.Proximo_Passo((dia + 2)+"/"+mes+"/"+ano)
    		.build();
    }

}