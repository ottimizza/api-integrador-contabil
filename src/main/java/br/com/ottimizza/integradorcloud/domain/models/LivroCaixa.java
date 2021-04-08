package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.array.ListArrayType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
@Entity
@Table(name = "livros_caixas")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class LivroCaixa implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "livro_caixa_sequence", sequenceName = "livros_caixas_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "livros_caixas_sequence")
	private BigInteger id;

	@Column(name = "cnpj_contabilidade")
	private String cnpjContabilidade;
	
	@Column(name = "cnpj_empresa")
	private String cnpjEmpresa;
	
	@Column(name = "data_movimento")
	private LocalDate dataMovimento;
	
	@Column(name = "data_previsao_pagamento")
	private LocalDate dataPrevisaoPagamento;

	@Column(name = "valor_original")
	private Double valorOriginal;

	@Column(name = "valor_pago")
	private Double valorPago;

	@Column(name = "valor_final")
	private Double valorFinal;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "fk_banco_id")
	private BigInteger bancoId;

	@Column(name = "fk_categoria_id")
	private BigInteger categoriaId;

	@Column(name = "tipo_movimento")
	private String tipoMovimento;

	@Column(name = "complemento")
	private String complemento;

	@Column(name = "link_arquivo")
	private String linkArquivo;

	@Column(name = "origem")
	private Integer origem;

	@Column(name = "integrado_contabilidade", columnDefinition = "boolean default false")
	private Boolean integradoContabilidade;

	@Column(name = "status")
	private Short status;

	@Column(name = "id_externo")
	private String idExterno;

	@Column(name = "texto_documento", columnDefinition = "varchar(800) default ''")
	private String textoDocumento;

	@Type(type = "list-array")
    @Column(name = "termos", columnDefinition = "varchar[]")
    private List<String> termos;

	@Column(name = "criado_por")
	private String criadoPor;

	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if(this.dataCriacao == null)
			this.dataCriacao = LocalDateTime.now(ZoneId.of("Brazil/East"));

		if(this.status == 0)
			this.valorFinal = this.valorOriginal;
		else
			this.valorFinal = this.valorPago;
			
		if(this.tipoMovimento.equals("PAG") && this.valorFinal > 0)
			this.valorFinal = this.valorFinal * -1;
		
	}

	public static class Status  {
        public static final Short ABERTO = 0;
        public static final Short PAGO = 1;
	}	
	
	public LivroCaixa(LivroCaixa livroCaixa) {
		this.cnpjContabilidade		= livroCaixa.getCnpjContabilidade();
		this.cnpjEmpresa			= livroCaixa.getCnpjEmpresa();
		this.dataMovimento			= livroCaixa.getDataMovimento();
		this.dataPrevisaoPagamento	= livroCaixa.getDataPrevisaoPagamento();
		this.valorOriginal			= livroCaixa.getValorOriginal();
		this.valorPago				= livroCaixa.getValorPago();
		this.valorFinal				= livroCaixa.getValorFinal();
		this.descricao				= livroCaixa.getDescricao();
		this.bancoId				= livroCaixa.getBancoId();
		this.categoriaId			= livroCaixa.getCategoriaId();
		this.tipoMovimento			= livroCaixa.getTipoMovimento();
		this.complemento			= livroCaixa.getComplemento();
		this.linkArquivo			= livroCaixa.getLinkArquivo();
		this.origem					= livroCaixa.getOrigem();
		this.status					= livroCaixa.getStatus();
		this.textoDocumento			= livroCaixa.getTextoDocumento();
		this.termos					= livroCaixa.getTermos();
		
		this.criadoPor				= livroCaixa.getCriadoPor();		
		this.integradoContabilidade	= false;
		this.dataCriacao			= LocalDateTime.now(ZoneId.of("Brazil/East"));
	}

	@Override
	public String toString() {
		return "{ \"id\":"+id+", \"cnpjContabilidade\":"+"\""+cnpjContabilidade+"\""+", \"cnpjEmpresa\":"+"\""+cnpjEmpresa+"\""+", \"dataMovimento\":"+"\""+dataMovimento+"\""+", \"dataPrevisaoPagamento\":"+"\""+dataPrevisaoPagamento+"\""+
				", \"valorOriginal\":"+valorOriginal+", \"valorPago\":"+valorPago+", \"valorFinal\":"+valorFinal+", \"descricao\":"+"\""+descricao+"\""+", \"bancoId\":"+bancoId+", \"categoriaId\":"+categoriaId+
				", \"tipoMovimento\":"+"\""+tipoMovimento+"\""+", \"complemento\":"+"\""+complemento+"\""+", \"linkArquivo\":"+"\""+linkArquivo+"\""+", \"origem\":"+origem+", \"integradoContabilidade\":"+integradoContabilidade+
				", \"status\":"+status+", \"textoDocumento\":"+"\""+textoDocumento+"\""+", \"termos\":"+"\""+termos+"\""+", \"criadoPor\":"+"\""+criadoPor+"\""+", \"dataCriacao\":"+"\""+dataCriacao+"\""+"}";
	}

}
