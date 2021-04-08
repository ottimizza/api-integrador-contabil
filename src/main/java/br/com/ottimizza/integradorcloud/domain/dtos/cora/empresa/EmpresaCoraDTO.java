package br.com.ottimizza.integradorcloud.domain.dtos.cora.empresa;

import java.io.Serializable;

import br.com.ottimizza.integradorcloud.domain.dtos.cora.empresa.CompanyCora;
import br.com.ottimizza.integradorcloud.domain.dtos.cora.empresa.OwnerCora;
import br.com.ottimizza.integradorcloud.domain.dtos.cora.empresa.PhoneCora;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class EmpresaCoraDTO implements Serializable {
	
	static final long serialVersionUID = 1L;
    
    private CompanyCora company;

    private PhoneCora phone;

    private OwnerCora owner;

}
