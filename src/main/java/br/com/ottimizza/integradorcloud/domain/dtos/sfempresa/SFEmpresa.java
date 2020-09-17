package br.com.ottimizza.integradorcloud.domain.dtos.sfempresa;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

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
	
	@JsonProperty(value = "Tipo_de_Projeto__c")
	private String Possui_OIC;
	
	@JsonProperty(value = "Resumo_Prox_Passo__c")
	private String Resumo_Prox_Passo;
	
	@JsonProperty(value = "Status_Projeto__c")
	private String Status_Projeto;
	
	@JsonProperty(value = "Codigo_Empresa_ERP__c")
	private String Codigo_Empresa_ERP;
	
	@JsonProperty(value = "Status_Report_Data__c")
	private LocalDateTime Status_Report_Data;
	
	@JsonProperty(value = "Contabilidade__c")
	private String Contabilidade_Id;
	
	@JsonProperty(value = "Name")
	private String Nome_Empresa;
	
	@JsonProperty(value = "CNPJ__c")
	private String Cnpj;
	
	@JsonProperty(value = "Nome_Resumido__c")
	private String Nome_Resumido;
	
	@JsonProperty(value = "Envolvidos__c")
	private String Envolvidos;
	
	@JsonProperty(value = "Proximo_Passo__c")
	private LocalDateTime Proximo_Passo;
	
	@JsonProperty(value = "Arquivo_Portal__c")
	private String Arquivo_Portal;
}
