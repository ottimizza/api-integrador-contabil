package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class RoteiroLayoutDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private BigInteger roteiroId;

    private BigInteger layoutId;

}
