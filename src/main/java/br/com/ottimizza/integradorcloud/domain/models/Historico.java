package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.jdo.annotations.Index;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "historicos")
@Index(members = { "contaMovimento", "cnpjEmpresa" })
public class Historico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "historicos_sequence", sequenceName = "historicos_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historicos_sequence")
    private BigInteger id;

    @Column(name = "conta_movimento", nullable = false)
    private String contaMovimento;

    @Column(name = "historico", nullable = false)
    private String historico;

    @Column(name = "cnpj_empresa", nullable = false)
    private String cnpjEmpresa;

    @Column(name = "cnpj_contabilidade", nullable = false)
    private String cnpjContabilidade;

    @Column(name = "data_criacao", nullable = false, columnDefinition = "timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Column(name = "data_atualizacao", nullable = false, columnDefinition = "timestamp default current_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        if (dataCriacao == null) {
            this.dataCriacao = new Date();
        }
        this.dataAtualizacao = new Date();
        this.cnpjContabilidade = this.cnpjContabilidade.replaceAll("\\D*", "");
        this.cnpjEmpresa = this.cnpjEmpresa.replaceAll("\\D*", "");
    }

}