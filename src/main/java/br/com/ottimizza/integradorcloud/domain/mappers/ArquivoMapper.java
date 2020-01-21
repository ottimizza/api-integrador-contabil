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
            .build();    
    }

    public static ArquivoDTO fromEntity(Arquivo arquivo) {
        return ArquivoDTO.builder()
                .id(arquivo.getId())
                .nome(arquivo.getNome())
                .cnpjEmpresa(arquivo.getCnpjEmpresa())
                .cnpjContabilidade(arquivo.getCnpjContabilidade())
            .build();
    }

    public static List<ArquivoDTO> fromEntities(List<Arquivo> arquivos) {
        return arquivos.stream().map(a -> fromEntity(a)).collect(Collectors.toList());
    }

}