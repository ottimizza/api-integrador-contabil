package br.com.ottimizza.integradorcloud.domain.dtos.sForce;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class SFContabilidade implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "CNPJ_Numeros__c")
	private String cnpj;
	
	@JsonProperty(value = "Name")
	private String name;
	
	@JsonProperty(value = "Id")
	private String id;
}
