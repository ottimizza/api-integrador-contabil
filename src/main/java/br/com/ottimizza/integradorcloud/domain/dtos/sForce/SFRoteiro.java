package br.com.ottimizza.integradorcloud.domain.dtos.sForce;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class SFRoteiro {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Empresa_a_Ser_Integrada__c")
	private String empresaId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Chave_OIC_Integracao__c")
	private String chaveOic;	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Tipo_Integracao__c")
	private String tipoIntegracao;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Nome_do_Relat_rio_Refer_ncia__c")
	private String nomeRelatorioReferencia;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Fornecedor__c")
	private String fornecedor;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Portador__c")
	private String portador;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Data_Movimento__c")
	private String dataMovimento;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Valor_Documento__c")
	private String valorDocumento;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Ler_planilhas_padroes__c")
	private Boolean lerPlanilhasPadroes;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Conta_Fixa_Juros__c")
	private String contaFixaJuros;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Conta_Fixa_Multa__c")
	private String contaFixaMulta;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Conta_Fixa_Desconto__c")
	private String contaFixaDesconto;
	
}
