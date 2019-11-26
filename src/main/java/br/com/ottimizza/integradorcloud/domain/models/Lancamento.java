package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class Lancamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "lancamentos_sequence", sequenceName = "lancamentos_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lancamentos_sequence")
    private BigInteger id;

    private LocalDate dataMovimento;

    //
    private String documento;

    private String descricao;

    private String portador;

    private String centroCusto;

    private String tipoPlanilha;

    //
    //
    private Short tipoLancamento;

    private Short tipoMovimento; // CTB/CTBJUR/CTBPORTADOR

    //
    //
    private String contaMovimento;

    private String contaContraPartida;

    private String historico;

    private String arquivo;

    //
    //
    private Double valorOriginal;

    private Double valorPago;

    private Double valorJuros;

    private Double valorDesconto;

    private Double valorMulta;

    //
    //
    private String complemento01;

    private String complemento02;

    private String complemento03;

    private String complemento04;

    private String complemento05;

    //
    //
    private String cnpjEmpresa;

    private String cnpjContabilidade;

    private String idRoteiro;

    //
    //
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = new Date();
        this.dataAtualizacao = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = new Date();
    }

}