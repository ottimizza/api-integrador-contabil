package br.com.ottimizza.integradorcloud.domain.dtos;

import java.math.BigInteger;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class LivroCaixaImportadoDTO {

    private String idExterno;

    private String tipoMovimento;

    private String descricao;

    private Double valor;

    private LocalDate data;

}
