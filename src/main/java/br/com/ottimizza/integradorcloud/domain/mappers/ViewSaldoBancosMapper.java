package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.ViewSaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.ViewSaldoBancos;

public class ViewSaldoBancosMapper {


    public static ViewSaldoBancosDTO fromEntity(ViewSaldoBancos saldo) {
        return ViewSaldoBancosDTO.builder()
            .id(saldo.getId())
            .banco(saldo.getBanco())
            .saldo(saldo.getSaldo())
            .data(saldo.getData())
            .cnpjEmpresa(saldo.getCnpjEmpresa())
        .build();
    }

    public static ViewSaldoBancos fromDTO(ViewSaldoBancosDTO saldo) {
        return ViewSaldoBancos.builder()
            .id(saldo.getId())
            .banco(saldo.getBanco())
            .saldo(saldo.getSaldo())
            .data(saldo.getData())
            .cnpjEmpresa(saldo.getCnpjEmpresa())
        .build();
    }

    public static List<ViewSaldoBancosDTO> fromEntities(List<ViewSaldoBancos> saldos) {
        return saldos.stream().map(saldo -> fromEntity(saldo)).collect(Collectors.toList());
    }
}