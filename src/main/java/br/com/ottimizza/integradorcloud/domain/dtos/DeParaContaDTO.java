package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeParaContaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String cnpjContabilidade;

    private String cnpjEmpresa;

    private String descricao;

    private String contaDebito;

    private String contaCredito;

    private String username;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

}
