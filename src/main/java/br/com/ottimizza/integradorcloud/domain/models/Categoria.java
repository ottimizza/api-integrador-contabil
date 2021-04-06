package br.com.ottimizza.integradorcloud.domain.models;

import java.math.BigInteger;

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
@Table(name = "categorias")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class Categoria {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "categorias_sequence", sequenceName = "categorias_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorias_sequence")
	private BigInteger id;

	@Column(name = "descricao")
	private String descricao;

} 