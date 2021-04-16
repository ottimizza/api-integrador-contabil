package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
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

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String dataString;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String descricaoBanco;
	
	public LivroCaixa patch(LivroCaixa livroCaixa) {
		
		if(complemento != null && !complemento.equals(""))
			livroCaixa.setComplemento(complemento);
		
		if(dataMovimento != null)
			livroCaixa.setDataMovimento(dataMovimento);
		
		if(valorOriginal != null)
			livroCaixa.setValorOriginal(valorOriginal);

		if(bancoId != null)
			livroCaixa.setBancoId(bancoId);

		if(descricao != null && !descricao.equals(""))
			livroCaixa.setDescricao(descricao);

		if(status != null) {
			livroCaixa.setStatus(status);
			
		}
		return livroCaixa;
	}

	@Override
	public String toString() {
		return "{ \"id\":"+id+", \"cnpjContabilidade\":"+"\""+cnpjContabilidade+"\""+", \"cnpjEmpresa\":"+"\""+cnpjEmpresa+"\""+", \"dataMovimento\":"+"\""+dataMovimento+"\""+", \"dataPrevisaoPagamento\":"+"\""+dataPrevisaoPagamento+"\""+
				", \"valorOriginal\":"+valorOriginal+", \"valorPago\":"+valorPago+", \"valorFinal\":"+valorFinal+", \"descricao\":"+"\""+descricao+"\""+", \"bancoId\":"+bancoId+", \"categoriaId\":"+categoriaId+
				", \"tipoMovimento\":"+"\""+tipoMovimento+"\""+", \"complemento\":"+"\""+complemento+"\""+", \"linkArquivo\":"+"\""+linkArquivo+"\""+", \"origem\":"+origem+", \"integradoContabilidade\":"+integradoContabilidade+
				", \"status\":"+status+", \"textoDocumento\":"+"\""+textoDocumento+"\""+", \"termos\":"+"\""+termos+"\""+", \"criadoPor\":"+"\""+criadoPor+"\""+", \"dataCriacao\":"+"\""+dataCriacao+"\""+", \"descricaoBanco\":"+"\""+descricaoBanco+"\"}";
	}

}
