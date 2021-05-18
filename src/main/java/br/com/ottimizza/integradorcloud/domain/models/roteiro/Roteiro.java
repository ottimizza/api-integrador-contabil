package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.*;

@Entity
@Data
@Table(name = "roteiros")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Roteiro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "roteiros_sequence", sequenceName = "roteiros_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roteiros_sequence")
	@Column(name = "id")
	private BigInteger id;

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "url_arquivo")
	private String urlArquivo;

	@Column(name = "fk_empresa_id")
	private BigInteger empresaId;

	@Column(name = "cnpj_empresa")
	private String cnpjEmpresa;

	@Column(name = "fk_contabilidade_id")
	private BigInteger contabilidadeId;

	@Column(name = "cnpj_contabilidade")
	private String cnpjContabilidade;

	@Column(name = "tipo_roteiro")
	private String tipoRoteiro;

	@Column(name = "status")
	private Short status;
	
	@Column(name = "tipo_projetoo")
	private Short tipoProjeto;

	@Column(name = "mapeamento")
	@Convert(converter = MapeamentoConverter.class)
	private Mapeamento mapeamento;
	
	@Column(name = "checklist")
	private Boolean checklist;

	@Column(name = "usuario")
	private String usuario;

	@Column(name = "layouts_padroes")
	private String layoutsPadroes;
	
	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;

	@PrePersist
	@PreUpdate 
	public void prePersist() {
		if(this.dataCriacao == null) {
			this.checklist = false;
			this.dataCriacao = LocalDateTime.now(ZoneId.of("Brazil/East"));
		}
		this.dataAtualizacao = LocalDateTime.now(ZoneId.of("Brazil/East"));
	}
}
