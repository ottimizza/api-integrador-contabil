package br.com.ottimizza.integradorcloud.domain.dtos;

import java.math.BigInteger;
import java.time.LocalDate;

import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ArquivoProntoDTO {

	private BigInteger id;

	private String cnpjContabilidade;

	private String cnpjEmpresa;

	private LocalDate dataMovimento;

	private String contaDebito;

	private String contaCredito;

	private String tipoLancamento;

	private String tipoMovimento;

	private String codigoHistorico;
	
	private String historico;

	private String erpContabil;

	private String lote;
	
	public ArquivoPronto patch(ArquivoPronto arquivo) {

		if(contaDebito != null && !contaDebito.equals(""))
			arquivo.setContaDebito(contaDebito);

		if(contaCredito != null && !contaCredito.equals(""))
			arquivo.setContaCredito(contaCredito);

		if(tipoLancamento != null  && !tipoLancamento.equals(""))
			arquivo.setTipoLancamento(tipoLancamento);

		if(tipoMovimento != null && !tipoMovimento.equals(""))
			arquivo.setTipoMovimento(tipoMovimento);

		if(historico != null && !historico.equals(""))
			arquivo.setHistorico(historico);

		if(codigoHistorico != null && !codigoHistorico.equals(""))
			arquivo.setCodigoHistorico(codigoHistorico);

		if(erpContabil != null && !erpContabil.equals(""))
			arquivo.setErpContabil(erpContabil);

		if(lote != null && !lote.equals(""))
			arquivo.setLote(lote);

		return arquivo;
	}
}