package br.com.ottimizza.integradorcloud.domain.dtos.lancamento;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

import br.com.ottimizza.integradorcloud.domain.dtos.arquivo.ArquivoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class LancamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private LocalDate dataMovimento;

    private String documento;

    private String descricao;

    private String portador;

    private String centroCusto;

    private ArquivoDTO arquivo;

    private String tipoPlanilha;

    private Short tipoLancamento;

    private String tipoMovimento; // CTB/CTBJUR/CTBPORTADOR

    private String contaMovimento;

    private String contaContraPartida;

    private Double valorOriginal;

    private Double valorPago;

    private Double valorJuros;

    private Double valorDesconto;

    private Double valorMulta;

    private String complemento01;

    private String complemento02;

    private String complemento03;

    private String complemento04;

    private String complemento05;

    private String cnpjEmpresa;

    private String cnpjContabilidade;

    private String idRoteiro;

}