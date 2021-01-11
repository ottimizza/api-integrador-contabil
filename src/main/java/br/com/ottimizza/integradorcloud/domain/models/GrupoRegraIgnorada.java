package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

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
@Data
@Entity
@Table(name = "grupo_regras_ignoradas")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GrupoRegraIgnorada implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	private BigInteger id;

    @Column(name = "posicao")
    private Integer posicao;
    
    @Column(name = "conta_movimento")
    private String contaMovimento;

    @Column(name = "tipo_lancamento")
    private Short tipoLancamento;

    @Column(name = "id_roteiro")
    private String idRoteiro;

    @Column(name = "cnpj_empresa")
    private String cnpjEmpresa;

    @Column(name = "cnpj_contabilidade")
    private String cnpjContabilidade;
    
    @Column(name = "contagem_regras")
    private Integer contagemRegras;
    
    @Column(name = "peso_regras")
    private Integer pesoRegras;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @Transient
    private List<Regra> regras;

    @Column(columnDefinition = "boolean default true")
    private Boolean ativo;

    @Column(name = "usuario")
    public String usuario;

    @Type(type = "list-array")
    @Column(name = "campos", columnDefinition = "varchar[]")
    private List<String> camposRegras;

}
