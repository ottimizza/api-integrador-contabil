package br.com.ottimizza.integradorcloud.domain.mappers.regra;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.regra.RegraDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Regra;

public class RegraMapper {

    public static Regra fromDto(RegraDTO regraDTO) {

        return Regra.builder()
                .id(regraDTO.getId())
                .campo(regraDTO.getCampo())
                .condicao(regraDTO.getCondicao())
                .valor(regraDTO.getValor())
                .grupoRegra(
                    GrupoRegra.builder().id(regraDTO.getGrupoRegraId()).build()
                )
            .build();    
    }

    public static RegraDTO fromEntity(Regra regra, boolean comGrupoRegra) {
        GrupoRegraDTO grupoRegra = GrupoRegraMapper.fromEntity(regra.getGrupoRegra());
        return RegraDTO.builder()
                .id(regra.getId())
                .campo(regra.getCampo())
                .condicao(regra.getCondicao())
                .valor(regra.getValor())
                .grupoRegra(
                    (comGrupoRegra && grupoRegra != null) 
                    ? grupoRegra : null
                )
                .grupoRegraId(
                    (!comGrupoRegra && grupoRegra != null) 
                    ? grupoRegra.getId() : null
                )
            .build();      
    }

}