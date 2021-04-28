package br.com.ottimizza.integradorcloud.domain.models.banco;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saldo_bancos")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class SaldoBancos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "saldo_sequence", sequenceName = "saldo_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saldo_sequence")
    private BigInteger id;

    @Column(name = "fk_banco_id")
    private BigInteger bancoId;
    
    @Column(name = "data")
    private LocalDate data;

    @Column(name = "saldo")
    private Double saldo;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;

    @PrePersist
	public void prePersist() {
		if(this.dataCriacao == null){
			this.dataCriacao = LocalDateTime.now(ZoneId.of("Brazil/East"));
        }
        this.dataAtualizacao = LocalDateTime.now(ZoneId.of("Brazil/East"));
    }
}
