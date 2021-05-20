package br.com.ottimizza.integradorcloud.domain.mappers;

import br.com.ottimizza.integradorcloud.domain.dtos.RoteiroLayoutDTO;
import br.com.ottimizza.integradorcloud.domain.models.roteiro_layouts.RoteiroLayout;

public class RoteiroLayoutMapper {
    

    public static RoteiroLayoutDTO fromEntity(RoteiroLayout roteiroLayout) {
        return RoteiroLayoutDTO.builder()
                .roteiroId(roteiroLayout.getRoteiro().getId())
                .layoutId(roteiroLayout.getLayout().getId())
            .build();
    }

}
