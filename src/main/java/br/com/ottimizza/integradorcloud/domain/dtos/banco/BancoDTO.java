package br.com.ottimizza.integradorcloud.domain.dtos.banco;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.ottimizza.integradorcloud.domain.models.banco.ObjetoAutenticacao;
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

	private String codigoBanco;

	private BigInteger bancoPadraoId;

	private ObjetoAutenticacao objetoAutenticacao;
}