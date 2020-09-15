package br.com.ottimizza.integradorcloud.domain.models.roteiro;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class MapeamentoCamposPlanilha implements Serializable {

	private static final long serialVersionUID = 1L;

	private CampoPlanilha[] dataMovimento;
	
	private CampoPlanilha[] documento;
	
	private CampoPlanilha[] descricao;
	
	private CampoPlanilha[] portador;
	
	private CampoPlanilha[] centroCusto;
	
	private CampoPlanilha[] tipoPlanilha;
	
	private CampoPlanilha[] valorOriginal;
	
	private CampoPlanilha[] valorPago;
	
	private CampoPlanilha[] valorJuros;
	
	private CampoPlanilha[] valorDesconto;
	
	private CampoPlanilha[] valorMulta;
	
	private CampoPlanilha[] complemento01;
	
	private CampoPlanilha[] complemento02;
	
	private CampoPlanilha[] complemento03;
	
	private CampoPlanilha[] complemento04;
	
	private CampoPlanilha[] complemento05;
	
}
