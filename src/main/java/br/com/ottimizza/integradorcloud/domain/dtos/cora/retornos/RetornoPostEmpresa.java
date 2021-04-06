package br.com.ottimizza.integradorcloud.domain.dtos.cora.retornos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class RetornoPostEmpresa implements Serializable {
	
	static final long serialVersionUID = 1L;
    
    private String id;

    private String code;

    private String deep_link;

    private String status;

}
