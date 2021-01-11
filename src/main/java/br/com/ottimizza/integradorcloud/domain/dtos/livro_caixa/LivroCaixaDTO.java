package br.com.ottimizza.integradorcloud.domain.dtos.livro_caixa;

import java.math.BigInteger;
import java.time.LocalDate;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class LivroCaixaDTO {

	private BigInteger id;
	
	private String cnpjContabilidade;
	
	private String cnpjEmpresa;
	
	private LocalDate dataMovimento;
	
	private Double valorEntrada;
	
	private Double valorSaida;
	
	private String fornecerdor;
	
	private String complemento;
	
	private String banco;
	
	public LivroCaixa patch(LivroCaixa livroCaixa) {
		
		if(valorEntrada != null)
			livroCaixa.setValorEntrada(valorEntrada);
		
		if(valorSaida != null)
			livroCaixa.setValorSaida(valorSaida);
		
		if(!fornecerdor.equals(""))
			livroCaixa.setFornecerdor(fornecerdor);
		
		if(!complemento.equals(""))
			livroCaixa.setComplemento(complemento);
		
		if(!banco.equals(""))
			livroCaixa.setBanco(banco);
		
		if(dataMovimento != null)
			livroCaixa.setDataMovimento(dataMovimento);
		
		return livroCaixa;
	}
}
