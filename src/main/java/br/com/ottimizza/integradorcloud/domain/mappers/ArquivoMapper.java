package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.arquivo.ArquivoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Arquivo;

public class ArquivoMapper { // @formatter:off

    public static Arquivo fromDto(ArquivoDTO arquivo) {
        return Arquivo.builder()
                .id(arquivo.getId())
                .nome(arquivo.getNome())
                .cnpjEmpresa(arquivo.getCnpjEmpresa())
                .cnpjContabilidade(arquivo.getCnpjContabilidade())
                .labelComplemento01(arquivo.getLabelComplemento01())
                .labelComplemento02(arquivo.getLabelComplemento02())
                .labelComplemento03(arquivo.getLabelComplemento03())
                .labelComplemento04(arquivo.getLabelComplemento04())
                .labelComplemento05(arquivo.getLabelComplemento05())
            .build();    
    }

    public static ArquivoDTO fromEntity(Arquivo arquivo) {
        return ArquivoDTO.builder()
                .id(arquivo.getId())
                .nome(arquivo.getNome())
                .cnpjEmpresa(arquivo.getCnpjEmpresa())
                .cnpjContabilidade(arquivo.getCnpjContabilidade())
                .labelComplemento01(arquivo.getLabelComplemento01())
                .labelComplemento02(arquivo.getLabelComplemento02())
                .labelComplemento03(arquivo.getLabelComplemento03())
                .labelComplemento04(arquivo.getLabelComplemento04())
                .labelComplemento05(arquivo.getLabelComplemento05())
            .build();
    }

    public static List<ArquivoDTO> fromEntities(List<Arquivo> arquivos) {
        return arquivos.stream().map(a -> fromEntity(a)).collect(Collectors.toList());
    }

}