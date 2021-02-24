package br.com.ottimizza.integradorcloud.domain.dtos.livro_caixa;


import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LivroCaixaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigInteger id;
	
	private String cnpjContabilidade;
	
	private String cnpjEmpresa;
	
	private LocalDate dataMovimento;
	
	private LocalDate dataPrevisaoPagamento;
	
	private Double valorOriginal;
	
	private Double valorPago;
	
	private Double valorFinal;
	
	private String descricao;

	private BigInteger bancoId;
	
	private BigInteger categoriaId;
	
	private String tipoMovimento;
	
	private String complemento;
	
	private String linkArquivo;
	
	private Integer origem;
	
	private Boolean integradoContabilidade;
	
	private Short status;
	
	private String textoDocumento;
	
    private List<String> termos;
	
	private String criadoPor;
	
	private LocalDateTime dataCriacao;
	
	private String dataString;
	
	private String urlArquivo;
	
	public LivroCaixa patch(LivroCaixa livroCaixa) {
		
		if(complemento != null && !complemento.equals(""))
			livroCaixa.setComplemento(complemento);

		if(dataMovimento != null)
			livroCaixa.setDataMovimento(dataMovimento);
		
		return livroCaixa;
	}
}
