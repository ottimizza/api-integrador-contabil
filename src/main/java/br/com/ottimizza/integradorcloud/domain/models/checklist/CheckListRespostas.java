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
@Table(name = "checklist_respostas")
public class CheckListRespostas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "respostas_sequence", sequenceName = "respostas_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "respostas_sequence")
	@Column(name = "id")
	private BigInteger id;
	
	@Column(name = "fk_pergunta_id")
	private BigInteger perguntaId;
	
	@Column(name = "fk_roteiro_id")
	private BigInteger roteiroId;
	
	@Column(name = "resposta")
	private String resposta;
	
	@Column(name = "observacao")
	private String observacoes;
}
