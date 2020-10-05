package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class HistoricoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String contaMovimento;

    private String historico;

    private String cnpjEmpresa;

    private String cnpjContabilidade;
    
    private Short tipoLancamento;
    
    private String idRoteiro;

    private Date dataCriacao;

    private Date dataAtualizacao;

    private Boolean ativo;

    private String usuario;

}