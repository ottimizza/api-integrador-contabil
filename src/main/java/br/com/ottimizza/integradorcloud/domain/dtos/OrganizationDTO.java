package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @formatter:off
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO implements Serializable {

    static final long serialVersionUID = 1L;
    
    
    private BigInteger id;

    
    private String externalId;

    
    private String name;

    
    private Integer type;

    
    private String cnpj;

   
    private String codigoERP;

    
    private String email;

    
    private String avatar;

    
    private OrganizationDTO organization;
    
    
    private BigInteger organizationId;

}
