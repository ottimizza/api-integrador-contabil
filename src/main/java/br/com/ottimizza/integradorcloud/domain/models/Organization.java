package br.com.ottimizza.integradorcloud.domain.models;

import java.math.BigInteger;

import br.com.ottimizza.integradorcloud.domain.OrganizationTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

	
    private BigInteger id;

    private String externalId;

    private String name;

    private Integer type = OrganizationTypes.CLIENT.getValue();

    private Boolean active;

    private String cnpj;

    private String codigoERP;

    private String email;

    private String avatar;

    private Organization organization;
	
}
