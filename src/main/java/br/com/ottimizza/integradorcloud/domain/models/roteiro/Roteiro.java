package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roteiros")
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class Roteiro {
	
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
