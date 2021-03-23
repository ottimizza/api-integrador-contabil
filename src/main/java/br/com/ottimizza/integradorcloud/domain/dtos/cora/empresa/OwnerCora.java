package br.com.ottimizza.integradorcloud.domain.dtos.cora.empresa;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class OwnerCora implements Serializable {
	
	static final long serialVersionUID = 1L;

    private String name;

    private String email;
    
}
