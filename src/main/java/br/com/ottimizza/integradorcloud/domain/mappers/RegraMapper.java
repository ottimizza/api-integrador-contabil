package br.com.ottimizza.integradorcloud.domain.mappers;


import br.com.ottimizza.integradorcloud.domain.dtos.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.RegraDTO;
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