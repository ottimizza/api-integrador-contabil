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
    
    @Column(name = "label_complemento_01", columnDefinition = "varchar(255) default 'Complemento 01'")
    private String labelComplemento01;

    @Column(name = "label_complemento_02", columnDefinition = "varchar(255) default 'Complemento 02'")
    private String labelComplemento02;
    
    @Column(name = "label_complemento_03", columnDefinition = "varchar(255) default 'Complemento 03'")
    private String labelComplemento03;
    
    @Column(name = "label_complemento_04", columnDefinition = "varchar(255) default 'Complemento 04'")
    private String labelComplemento04;

    @Column(name = "label_complemento_05", columnDefinition = "varchar(255) default 'Complemento 05'")    
    private String labelComplemento05;

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