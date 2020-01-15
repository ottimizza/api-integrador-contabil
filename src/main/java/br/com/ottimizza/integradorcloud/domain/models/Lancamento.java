package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "lancamentos")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class Lancamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "lancamentos_sequence", sequenceName = "lancamentos_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lancamentos_sequence")
    private BigInteger id;

    private LocalDate dataMovimento;

    @Column(columnDefinition = "varchar(255) default ''")
    private String documento;

    @Column(columnDefinition = "varchar(255) default ''")
    private String descricao;

    @Column(columnDefinition = "varchar(255) default ''")
    private String portador;

    @Column(columnDefinition = "varchar(255) default ''")
    private String centroCusto;

    private String tipoPlanilha;

    private Short tipoLancamento;

    private String tipoMovimento; // CTB/CTBJUR/CTBPORTADOR

    private String contaMovimento;

    private String contaContraPartida;

    private String contaSugerida;

    private Short tipoConta;

    private String historico;

    @ManyToOne
    @JoinColumn(name = "fk_arquivos_id", nullable = true)
    private Arquivo arquivo;

    @Column(columnDefinition="decimal(10,2) default '0.00'")
    private Double valorOriginal;

    @Column(columnDefinition="decimal(10,2) default '0.00'")
    private Double valorPago;

    @Column(columnDefinition="decimal(10,2) default '0.00'")
    private Double valorJuros;

    @Column(columnDefinition="decimal(10,2) default '0.00'")
    private Double valorDesconto;
    
    @Column(columnDefinition="decimal(10,2) default '0.00'")
    private Double valorMulta;

    @Column(columnDefinition = "varchar(255) default ''")
    private String complemento01;

    @Column(columnDefinition = "varchar(255) default ''")
    private String complemento02;

    @Column(columnDefinition = "varchar(255) default ''")
    private String complemento03;

    @Column(columnDefinition = "varchar(255) default ''")
    private String complemento04;

    @Column(columnDefinition = "varchar(255) default ''")
    private String complemento05;

    private String cnpjEmpresa;

    private String cnpjContabilidade;

    private String idRoteiro;

    //
    //
    //
    private BigInteger organizationId;
    
    private BigInteger accountingId;

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
        
        this.cnpjContabilidade = this.cnpjContabilidade.replaceAll("\\D*", "");
        this.cnpjEmpresa = this.cnpjEmpresa.replaceAll("\\D*", "");

        if (documento == null) documento = "";
        if (descricao == null) descricao = "";
        if (portador == null) portador = "";
        if (centroCusto == null) centroCusto = "";

        if (valorPago == null) valorPago = 0.00;
        if (valorJuros == null) valorJuros = 0.00;
        if (valorDesconto == null) valorDesconto = 0.00;
        if (valorMulta == null) valorMulta = 0.00;

        if (complemento01 == null) complemento01 = "";
        if (complemento03 == null) complemento03 = "";
        if (complemento03 == null) complemento03 = "";
        if (complemento04 == null) complemento04 = "";
        if (complemento05 == null) complemento05 = "";

    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = new Date();
        this.cnpjContabilidade = this.cnpjContabilidade.replaceAll("\\D*", "");
        this.cnpjEmpresa = this.cnpjEmpresa.replaceAll("\\D*", "");
    }

    public static class Tipo { 
        public static final Short PAGAMENTO = 1;
        public static final Short RECEBIMENTO = 2;
    }

    public static class TipoConta { 
        public static final Short DEPARA        = 1;
        public static final Short OUTRAS_CONTAS = 2;
        public static final Short IGNORAR       = 3;
        public static final Short PULAR         = 4;
    }

}