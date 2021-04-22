package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.LayoutPadraoDTO;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;


public class LayoutsPadroesMapper {
    
    public static LayoutPadrao fromDTO(LayoutPadraoDTO layout) {
        return LayoutPadrao.builder()
                .id(layout.getId())
                .idSalesForce(layout.getIdSalesForce())
                .linkReferencia(layout.getLinkReferencia())
                .descricaoDocumento(layout.getDescricaoDocumento())
                .tipoIntegracao(layout.getTipoIntegracao())
                .tipoArquivo(layout.getTipoArquivo())
                .pagamentos(layout.getPagamentos())
                .recebimentos(layout.getRecebimentos())
            .build();
    }

    public static LayoutPadraoDTO fromEntity(LayoutPadrao layout) {
        return LayoutPadraoDTO.builder()
                .id(layout.getId())
                .idSalesForce(layout.getIdSalesForce())
                .linkReferencia(layout.getLinkReferencia())
                .descricaoDocumento(layout.getDescricaoDocumento())
                .tipoIntegracao(layout.getTipoIntegracao())
                .tipoArquivo(layout.getTipoArquivo())
                .pagamentos(layout.getPagamentos())
                .recebimentos(layout.getRecebimentos())
            .build();
    }

    public static List<LayoutPadraoDTO> fromEntities(List<LayoutPadrao> layouts) {
        return layouts.stream().map(layout -> fromEntity(layout)).collect(Collectors.toList());
    }

}
