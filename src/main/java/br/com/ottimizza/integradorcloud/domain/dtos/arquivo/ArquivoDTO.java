package br.com.ottimizza.integradorcloud.domain.dtos.arquivo;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class ArquivoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String nome;

    private String cnpjEmpresa;

    private String cnpjContabilidade;


}