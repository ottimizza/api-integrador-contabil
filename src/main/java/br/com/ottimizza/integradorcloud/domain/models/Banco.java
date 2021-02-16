package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "bancos", indexes = {@Index(name = "banco_index", columnList = "id", unique = true)})
public class Banco implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false)
	@SequenceGenerator(name = "banco_sequence", sequenceName = "banco_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banco_sequence")
	private BigInteger id;
	
	@Column(length = 14)
	private String cnpjContabilidade;

	@Column(length = 14)
	private String cnpjEmpresa;
	
	private String nomeBanco;
	
	private String descricao;

}
