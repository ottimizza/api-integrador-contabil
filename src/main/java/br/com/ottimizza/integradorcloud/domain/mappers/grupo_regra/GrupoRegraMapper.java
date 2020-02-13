package br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;

public class GrupoRegraMapper {

    public static GrupoRegra fromDto(GrupoRegraDTO grupoRegraDTO) {
        return GrupoRegra.builder()
                .id(grupoRegraDTO.getId())
                .posicao(grupoRegraDTO.getPosicao())
                .contaMovimento(grupoRegraDTO.getContaMovimento())
                .tipoLancamento(grupoRegraDTO.getTipoLancamento())
                .idRoteiro(grupoRegraDTO.getIdRoteiro())
                .cnpjEmpresa(grupoRegraDTO.getCnpjEmpresa())
                .dataCriacao(grupoRegraDTO.getDataCriacao())
                .dataAtualizacao(grupoRegraDTO.getDataAtualizacao())
                .cnpjContabilidade(grupoRegraDTO.getCnpjContabilidade())
            .build();    
    }

    public static GrupoRegraDTO fromEntity(GrupoRegra grupoRegra) {
        return GrupoRegraDTO.builder()
                .id(grupoRegra.getId())
                .posicao(grupoRegra.getPosicao())
                .contaMovimento(grupoRegra.getContaMovimento())
                .tipoLancamento(grupoRegra.getTipoLancamento())
                .idRoteiro(grupoRegra.getIdRoteiro())
                .cnpjEmpresa(grupoRegra.getCnpjEmpresa())
                .cnpjContabilidade(grupoRegra.getCnpjContabilidade())
                .dataCriacao(grupoRegra.getDataCriacao())
                .dataAtualizacao(grupoRegra.getDataAtualizacao())
            .build();   
    }

}