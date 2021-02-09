package br.com.ottimizza.integradorcloud.domain.dtos.lote_processado;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class LoteProcessadoDTO {

	private BigInteger id;

	private String cnpjContabilidade;

	private String contabilidade;

	private String cnpjEmpresa;

	private String empresa;

	private String codigoErp;

	private String erpContabil;

	private String lote;

	private Long quantidadeLancamentos;

	private String tipoRegistro;

	private LocalDate anoMes;

	private LocalDateTime dataCriacao;

	private LocalDateTime dataAtualizacao;

}
