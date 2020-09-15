package br.com.ottimizza.integradorcloud.domain.models.checklist;


import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Convert;
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
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "checklist_perguntas")
public class CheckListPerguntas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "perguntas_sequence", sequenceName = "perguntas_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perguntas_sequence")
	@Column(name = "id")
	private BigInteger id;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "tipo")
	private Short tipo;
	
	@Column(name = "tipo_input")
	private Short tipoInput;
	
	@Column(name = "opcoes_respostas")
	@Convert(converter = PerguntasOpcoesRespostaConverter.class)
	private PerguntasOpcoesResposta[] opcoesResposta;
	
	@Column(name = "sugestao")
	private String sugestao;
	
	@Column(name = "grupo")
	private String grupo;
	
}
