package br.com.ottimizza.integradorcloud.domain.mappers.historico;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Historico;

public class HistoricoMapper { // @formatter:off

    public static Historico fromDto(HistoricoDTO historico) {
        return Historico.builder()
                .id(historico.getId())
                .contaMovimento(historico.getContaMovimento())
                .historico(historico.getHistorico())
                .cnpjEmpresa(historico.getCnpjEmpresa())
                .cnpjContabilidade(historico.getCnpjContabilidade())
                .dataCriacao(historico.getDataCriacao())
                .dataAtualizacao(historico.getDataAtualizacao())
            .build();    
    }

    public static HistoricoDTO fromEntity(Historico historico) {
        return HistoricoDTO.builder()
                .id(historico.getId())
                .contaMovimento(historico.getContaMovimento())
                .historico(historico.getHistorico())
                .cnpjEmpresa(historico.getCnpjEmpresa())
                .cnpjContabilidade(historico.getCnpjContabilidade())
                .dataCriacao(historico.getDataCriacao())
                .dataAtualizacao(historico.getDataAtualizacao())
            .build();
    }

    public static List<HistoricoDTO> fromEntities(List<Historico> historicos) {
        return historicos.stream().map(h -> fromEntity(h)).collect(Collectors.toList());
    }

}