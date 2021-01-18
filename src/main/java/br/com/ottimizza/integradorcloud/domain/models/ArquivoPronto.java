package br.com.ottimizza.integradorcloud.domain.models;

import java.math.BigInteger;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "arquivo_pronto")
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ArquivoPronto {
	
	@Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "arquivo_pronto_sequence", sequenceName = "arquivo_pronto_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arquivo_pronto_sequence")
    private BigInteger id;
	
	@Column(name = "cnpj_contabilidade")
	private String cnpjContabilidade;
	
	@Column(name = "cnpj_empresa")
	private String cnpjEmpresa;
	
	@Column(name = "data_movimento")
	private LocalDate dataMovimento;
	
	@Column(name = "conta_debito")
	private String contaDebito;
	
	@Column(name = "conta_credito")
	private String contaCredito;
	
	@Column(name = "tipo_lancamento")
	private String tipoLancamento;
	
	@Column(name = "tipo_movimento")
	private String tipoMovimento;
	
	@Column(name = "codigo_historico")
	private String codigoHistorico;
	
	@Column(name = "erp_contabil")
	private String erpContabil;
	
	@Column(name = "lote")
	private String lote;

}
