package br.com.ottimizza.integradorcloud.domain.models.banco;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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

	private String codigoBanco;

	@Column(name = "fk_banco_padrao_id")
	private BigInteger bancoPadraoId;

	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private ObjetoAutenticacao objetoAutenticacao;

	@PrePersist @PreUpdate
    public void prePersist() {
    	descricao = codigoBanco+" - "+nomeBanco.toUpperCase(); 
    }

}