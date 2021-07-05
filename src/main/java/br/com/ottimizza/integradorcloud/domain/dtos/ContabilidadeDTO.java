package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor@AllArgsConstructor
@Builder(toBuilder = true)
public class ContabilidadeDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private BigInteger id;
	
	private String cnpj;
	
	private String nome;
	
	private String salesForceId;
	
	private BigInteger ouathId;

	private Short parceiro;

	private String urlParceiro;

	private String token;

	private String celular;

    public Contabilidade patch(Contabilidade contabilidade) {

        if(nome != null && !nome.equals(""))
            contabilidade.setNome(nome);

        if(parceiro != null)
            contabilidade.setParceiro(parceiro);

        if(urlParceiro != null && !urlParceiro.equals(""))
            contabilidade.setUrlParceiro(urlParceiro);
            
        if(token != null && !token.equals("")) 
            contabilidade.setToken(token);

        if(celular != null && !celular.equals(""))
            contabilidade.setCelular(celular);

        return contabilidade;
    }

}
