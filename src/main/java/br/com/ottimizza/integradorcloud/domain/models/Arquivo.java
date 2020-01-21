package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

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
@Entity
@Table(name = "arquivos")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "arquivos_sequence", sequenceName = "arquivos_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arquivos_sequence")
    private BigInteger id;

    private String nome;

    private String cnpjEmpresa;

    private String cnpjContabilidade;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
        
        this.cnpjContabilidade = this.cnpjContabilidade.replaceAll("\\D*", "");
        this.cnpjEmpresa = this.cnpjEmpresa.replaceAll("\\D*", "");
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = new Date();
        
        this.cnpjContabilidade = this.cnpjContabilidade.replaceAll("\\D*", "");
        this.cnpjEmpresa = this.cnpjEmpresa.replaceAll("\\D*", "");
    }

}