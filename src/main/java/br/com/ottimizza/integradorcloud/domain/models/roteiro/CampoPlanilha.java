package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class CampoPlanilha implements Serializable {

	private static final long serialVersionUID = 1L;

	private String campo;
	
	private String valor;
	
	private String coluna;
	
}
