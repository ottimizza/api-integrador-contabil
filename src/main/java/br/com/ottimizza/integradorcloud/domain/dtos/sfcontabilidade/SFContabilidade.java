package br.com.ottimizza.integradorcloud.domain.dtos.sfcontabilidade;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class SFContabilidade {
	
	@JsonProperty(value = "CNPJ_Numeros__c")
	private String cnpj;
	
	@JsonProperty(value = "Name")
	private String name;
	
	@JsonProperty(value = "Id")
	private String id;
}
