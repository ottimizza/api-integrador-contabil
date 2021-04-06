package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancosPadroesDTO;
import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;

public class BancosPadroesMapper {

    public static BancosPadroes fromDTO(BancosPadroesDTO banco) {
        return BancosPadroes.builder()
                .id(banco.getId())
                .nomeBanco(banco.getNomeBanco())
                .codigoBanco(banco.getCodigoBanco())
                .descricao(banco.getDescricao())
                .iconeBanco(banco.getIconeBanco())
                .objetoAutenticacao(banco.getObjetoAutenticacao())
            .build();
    }

    public static BancosPadroesDTO fromEntity(BancosPadroes banco) {
        return BancosPadroesDTO.builder()
                .id(banco.getId())
                .nomeBanco(banco.getNomeBanco())
                .codigoBanco(banco.getCodigoBanco())
                .descricao(banco.getDescricao())
                .iconeBanco(banco.getIconeBanco())
                .objetoAutenticacao(banco.getObjetoAutenticacao())
            .build();
    }

    public static List<BancosPadroesDTO> fromEntities(List<BancosPadroes> Bancos) {
        return Bancos.stream().map(banco -> fromEntity(banco)).collect(Collectors.toList());
    }

}