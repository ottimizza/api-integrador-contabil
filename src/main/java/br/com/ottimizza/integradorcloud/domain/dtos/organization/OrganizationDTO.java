package br.com.ottimizza.integradorcloud.domain.dtos.organization;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @formatter:off
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO implements Serializable {

    static final long serialVersionUID = 1L;
    
    @Getter @Setter
    private BigInteger id;

    @Getter @Setter
    private String externalId;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Integer type;

    @Getter @Setter
    private String cnpj;

    @Getter @Setter
    private String codigoERP;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String avatar;

    @Getter @Setter
    private BigInteger organizationId;

}
