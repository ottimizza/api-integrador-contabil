package br.com.ottimizza.integradorcloud.domain.models.checklist;

import java.io.Serializable;
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

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "checklist_observacoes")
public class CheckListObservacoes implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "observacoes_sequence", sequenceName = "observacoes_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "observacoes_sequence")
	@Column(name = "id")
	private BigInteger id;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "importante")
	private Boolean importante;
}
