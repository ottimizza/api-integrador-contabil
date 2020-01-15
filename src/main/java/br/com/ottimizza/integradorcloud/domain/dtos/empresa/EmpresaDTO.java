package br.com.ottimizza.integradorcloud.domain.dtos.empresa;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private String cnpj;

    private String razaoSocial;

    private String codigoERP;

    private BigInteger organizationId;

    private BigInteger accountingId;

}