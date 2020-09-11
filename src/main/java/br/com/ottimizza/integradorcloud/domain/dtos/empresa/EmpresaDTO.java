package br.com.ottimizza.integradorcloud.domain.dtos.empresa;

import java.io.Serializable;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String cnpj;

    private String nomeResumido;
    
    private String razaoSocial;

    private String codigoERP;
    
    private String nomeCompleto;

    private BigInteger organizationId;

    private BigInteger accountingId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contabilidadeCrmId;
    

}