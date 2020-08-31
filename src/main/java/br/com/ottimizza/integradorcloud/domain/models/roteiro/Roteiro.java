package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "roteiros")
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class Roteiro {

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

	@Column(name = "mapeamento")
	@Convert(converter = MapeamentoConverter.class)
	private Mapeamento mapeamento;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;

	@PrePersist
	@PreUpdate 
	public void prePersist() {
		if(this.dataCriacao == null) {
			this.dataCriacao = LocalDateTime.now();
		}
		this.dataAtualizacao = LocalDateTime.now();
	}
}
