package br.com.ottimizza.integradorcloud.domain.dtos;

import java.math.BigInteger;

import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class LayoutPadraoDTO {
    
    private BigInteger id;

    private String idSalesForce;

    private String linkReferencia;

    private String descricaoDocumento;

    private Short tipoIntegracao;

    private String tipoArquivo;

    private Boolean pagamentos;

    private Boolean recebimentos;

    public LayoutPadrao patch(LayoutPadrao layout) {
		
		if(idSalesForce != null && !idSalesForce.equals(""))
            layout.setIdSalesForce(idSalesForce);
		
		if(linkReferencia != null && !linkReferencia.equals(""))
            layout.setLinkReferencia(linkReferencia);
		
		if(descricaoDocumento != null && !descricaoDocumento.equals(""))
            layout.setDescricaoDocumento(descricaoDocumento);

		if(tipoIntegracao != null)
            layout.setTipoIntegracao(tipoIntegracao);

		if(tipoArquivo != null && !tipoArquivo.equals(""))
            layout.setTipoArquivo(tipoArquivo);

		if(pagamentos != null) {
			layout.setPagamentos(pagamentos);
       }

        if(pagamentos != null) {
             layout.setPagamentos(pagamentos);
        } 
		
		return layout;
	}

}
