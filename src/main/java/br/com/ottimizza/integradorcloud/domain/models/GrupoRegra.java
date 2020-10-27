package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "grupo_regras")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class GrupoRegra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "grupo_regras_sequence", sequenceName = "grupo_regras_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupo_regras_sequence")
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

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        if (this.dataCriacao == null) {
            this.dataCriacao = new Date();
            this.ativo = true;
        }      
        this.dataAtualizacao = new Date();
        this.cnpjContabilidade = this.cnpjContabilidade.replaceAll("\\D*", "");
        this.cnpjEmpresa = this.cnpjEmpresa.replaceAll("\\D*", "");
    }

    public static class Tipo { 
        public static final Short PAGAMENTO = 1;
        public static final Short RECEBIMENTO = 2;
    }

}