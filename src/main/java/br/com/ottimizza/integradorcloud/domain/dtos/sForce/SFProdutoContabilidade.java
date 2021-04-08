package br.com.ottimizza.integradorcloud.domain.dtos.sForce;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SFProdutoContabilidade implements Serializable {

	static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "Id")
	private String idProduto;

}