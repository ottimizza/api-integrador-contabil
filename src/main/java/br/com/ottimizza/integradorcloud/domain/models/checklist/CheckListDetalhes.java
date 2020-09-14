package br.com.ottimizza.integradorcloud.domain.models.checklist;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CheckListDetalhes {

	private BigInteger id;
	
	private String descricao;
	
	private Boolean importante;
}
