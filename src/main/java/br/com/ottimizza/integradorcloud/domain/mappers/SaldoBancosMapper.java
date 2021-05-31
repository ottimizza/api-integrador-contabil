package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.SaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;

public class SaldoBancosMapper {
    
    public static SaldoBancos fromDTO(SaldoBancosDTO saldo) {
        return SaldoBancos.builder()
                .id(saldo.getId())
                .bancoId(saldo.getBancoId())
                .data(saldo.getData())
                .saldo(saldo.getSaldo())
                .dataCriacao(saldo.getDataCriacao())
                .dataAtualizacao(saldo.getDataAtualizacao())
            .build();
    }

    public static SaldoBancosDTO fromEntity(SaldoBancos saldo) {
        return SaldoBancosDTO.builder()
                .id(saldo.getId())
                .bancoId(saldo.getBancoId())
                .data(saldo.getData())
                .saldo(saldo.getSaldo())
                .dataCriacao(saldo.getDataCriacao())
                .dataAtualizacao(saldo.getDataAtualizacao())
            .build();
    }

    public static List<SaldoBancosDTO> fromEntities(List<SaldoBancos> saldos) {
        return saldos.stream().map(saldo -> fromEntity(saldo)).collect(Collectors.toList());
    }

}
