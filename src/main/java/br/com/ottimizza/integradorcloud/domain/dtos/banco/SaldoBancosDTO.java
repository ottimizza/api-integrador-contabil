package br.com.ottimizza.integradorcloud.domain.dtos.banco;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class SaldoBancosDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private BigInteger id;

    private BigInteger bancoId;
    
    private LocalDate data;

    private Double saldo;

    private LocalDateTime dataCriacao;

	private LocalDateTime dataAtualizacao;
}
