package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BancoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private BigInteger id;
	
	private String cnpjContabilidade;

	private String cnpjEmpresa;
	
	private String nomeBanco;
	
	private String descricao;
}
