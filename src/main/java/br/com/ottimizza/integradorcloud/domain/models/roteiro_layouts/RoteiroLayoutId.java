package br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class RoteiroLayoutId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "fk_roteiro_id")
    private BigInteger roteiroId;

    @Column(name = "fk_layout_id")
    private BigInteger layoutId;

}
