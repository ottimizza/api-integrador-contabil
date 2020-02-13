package br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;

public class GrupoRegraMapper {

    public static GrupoRegra fromDto(GrupoRegraDTO grupoRegraDTO) {
        Date dataCriacao = Date.from(grupoRegraDTO.getDataCriacao().atZone(ZoneId.systemDefault()).toInstant());
        Date dataAtualizacao = Date.from(grupoRegraDTO.getDataAtualizacao().atZone(ZoneId.systemDefault()).toInstant());

        return GrupoRegra.builder()
                .id(grupoRegraDTO.getId())
                .posicao(grupoRegraDTO.getPosicao())
                .contaMovimento(grupoRegraDTO.getContaMovimento())
                .tipoLancamento(grupoRegraDTO.getTipoLancamento())
                .idRoteiro(grupoRegraDTO.getIdRoteiro())
                .cnpjEmpresa(grupoRegraDTO.getCnpjEmpresa())
                .dataCriacao(dataCriacao)
                .dataAtualizacao(dataAtualizacao)
                .cnpjContabilidade(grupoRegraDTO.getCnpjContabilidade())
            .build();    
    }

    public static GrupoRegraDTO fromEntity(GrupoRegra grupoRegra) {

        LocalDateTime dataCriacao = grupoRegra.getDataCriacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dataAtualizacao = grupoRegra.getDataAtualizacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return GrupoRegraDTO.builder()
                .id(grupoRegra.getId())
                .posicao(grupoRegra.getPosicao())
                .contaMovimento(grupoRegra.getContaMovimento())
                .tipoLancamento(grupoRegra.getTipoLancamento())
                .idRoteiro(grupoRegra.getIdRoteiro())
                .cnpjEmpresa(grupoRegra.getCnpjEmpresa())
                .cnpjContabilidade(grupoRegra.getCnpjContabilidade())
                .dataCriacao(dataCriacao)
                .dataAtualizacao(dataAtualizacao)
            .build();   
    }

}