package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class SFEmpresa implements Serializable {
	
	static final long serialVersionUID = 1L;

	public static final String S_NAME = "Empresa__c";
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Tipo_de_Projeto__c")
	private String Possui_OIC;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Resumo_Prox_Passo__c")
	private String Resumo_Prox_Passo;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Status_Projeto__c")
	private String Status_Projeto;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Codigo_Empresa_ERP__c")
	private String Codigo_Empresa_ERP;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Status_Report_Data__c")
	private LocalDateTime Status_Report_Data;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Contabilidade__c")
	private String Contabilidade_Id;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Name")
	private String Nome_Empresa;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "CNPJ__c")
	private String Cnpj;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Nome_Resumido__c")
	private String Nome_Resumido;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Envolvidos__c")
	private String Envolvidos;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Proximo_Passo__c")
	private LocalDateTime Proximo_Passo;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Arquivo_Portal__c")
	private String Arquivo_Portal;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Email_de_quem_faz_o_fechamento__c")
	private String Email_de_quem_faz_o_fechamento;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Quem__c")
	private String Nome_de_quem_faz_o_fechamento;
	
}
