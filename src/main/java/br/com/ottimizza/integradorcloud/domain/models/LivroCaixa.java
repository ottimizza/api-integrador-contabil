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

@Entity
@Table(name = "livro_caixa")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class LivroCaixa {
	
	
	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "livro_caixa_sequence", sequenceName = "livro_caixa_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "livro_caixa_sequence")
	private BigInteger id;
	
	@Column(name = "cnpj_contabilidade")
	private String cnpjContabilidade;
	
	@Column(name = "cnpj_empresa")
	private String cnpjEmpresa;
	
	@Column(name = "data_movimento")
	private LocalDate dataMovimento;
	
	@Column(name = "valor_entrada")
	private Double valorEntrada;
	
	@Column(name = "valor_saida")
	private Double valorSaida;
	
	@Column(name = "fornecedor")
	private String fornecerdor;
	
	@Column(name = "complemento")
	private String complemento;
	
	@Column(name = "banco")
	private String banco;

	
}
