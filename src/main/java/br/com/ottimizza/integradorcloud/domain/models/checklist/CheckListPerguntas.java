package br.com.ottimizza.integradorcloud.domain.models.checklist;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.array.ListArrayType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;


@TypeDef(
		name = "list-array",
		typeClass = ListArrayType.class
)

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
	private String tipo;

	@Column(name = "tipo_input")
	private Short tipoInput;

	@Column(name = "opcoes_respostas")
	@Convert(converter = PerguntasOpcoesRespostaConverter.class)
	private PerguntasOpcoesResposta[] opcoesResposta;

	@Column(name = "sugestao")
	private String sugestao;

	@Column(name = "grupo")
	private String grupo;

	@Type(type = "list-array")
	@Column(name = "perguntas_relacionadas", columnDefinition = "bigint[]")
	private List<Long> perguntasRelacionadas;

}
