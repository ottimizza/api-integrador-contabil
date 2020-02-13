package br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.utils.DateUtils;

public class GrupoRegraMapper {

    public static GrupoRegra fromDto(GrupoRegraDTO grupoRegraDTO) {
        return GrupoRegra.builder()
                .id(grupoRegraDTO.getId())
                .posicao(grupoRegraDTO.getPosicao())
                .contaMovimento(grupoRegraDTO.getContaMovimento())
                .tipoLancamento(grupoRegraDTO.getTipoLancamento())
                .idRoteiro(grupoRegraDTO.getIdRoteiro())
                .cnpjEmpresa(grupoRegraDTO.getCnpjEmpresa())
                .dataCriacao(DateUtils.toDate(grupoRegraDTO.getDataCriacao()))
                .dataAtualizacao(DateUtils.toDate(grupoRegraDTO.getDataAtualizacao()))
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
                .dataCriacao(DateUtils.toLocalDateTime(grupoRegra.getDataCriacao()))
                .dataAtualizacao(DateUtils.toLocalDateTime(grupoRegra.getDataAtualizacao()))
            .build();   
    }

}