package br.com.ottimizza.integradorcloud.domain.dtos.regra;

import java.io.Serializable;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegraDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String campo;

    private Short condicao;

    private String valor;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GrupoRegraDTO grupoRegra;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigInteger grupoRegraId;

}