package br.com.ottimizza.integradorcloud.domain.dtos.sForce;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SFHistorico implements Serializable {

	public static final String S_NAME = "Roteiros_vs_Historio__c";

	public static final String RECORD_TYPE_ID = "0121500000100GB";

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "ID_Externo__c")
	private String ID_Externo__c;

	@JsonProperty(value = "RecordTypeId")
	private String RecordTypeId; // RECORD_TYPE_ID

	@JsonProperty(value = "Roteiro__c")
	private String Roteiro__c;

	@JsonProperty(value = "Codigo_Historico__c")
	private String Codigo_Historico__c;

	@JsonProperty(value = "Se_Campo__c")
	private String Se_Campo__c;

	@JsonProperty(value = "O_texto_01__c")
	private String O_texto_01__c;

	@JsonProperty(value = "Texto_01__c")
	private String Texto_01__c;

	@JsonProperty(value = "Campo_01__c")
	private String Campo_01__c;

	@JsonProperty(value = "Texto_02__c")
	private String Texto_02__c;

	@JsonProperty(value = "Campo_02__c")
	private String Campo_02__c;
	
	@JsonProperty(value = "Texto_03__c")
	private String Texto_03__c;

	@JsonProperty(value = "Campo_03__c")
	private String Campo_03__c;

	@JsonProperty(value = "Texto_04__c")
	private String Texto_04__c;

	@JsonProperty(value = "Campo_04__c")
	private String Campo_04__c;
	
	@JsonProperty(value = "Texto_05__c")
	private String Texto_05__c;
	
	@JsonProperty(value = "Campo_05__c")
	private String Campo_05__c;
}
