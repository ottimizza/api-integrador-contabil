package br.com.ottimizza.integradorcloud.domain.dtos;

import java.math.BigInteger;
import java.time.LocalDate;

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

	private String erpContabil;

	private String lote;
}