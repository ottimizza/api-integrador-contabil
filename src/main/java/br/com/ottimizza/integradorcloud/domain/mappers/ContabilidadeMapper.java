package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.ContabilidadeDTO;
import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;

public class ContabilidadeMapper {
    
    public static Contabilidade fromDTO(ContabilidadeDTO contabilidade) {
        return Contabilidade.builder()
                .id(contabilidade.getId())
                .cnpj(contabilidade.getCnpj())
                .nome(contabilidade.getNome())
                .ouathId(contabilidade.getOuathId())
                .salesForceId(contabilidade.getSalesForceId())
                .celular(contabilidade.getCelular())
                .parceiro(contabilidade.getParceiro())
                .urlParceiro(contabilidade.getUrlParceiro())
                .token(contabilidade.getToken())
            .build();
    }

    public static ContabilidadeDTO fromEntity(Contabilidade contabilidade) {
        return ContabilidadeDTO.builder()
                .id(contabilidade.getId())
                .cnpj(contabilidade.getCnpj())
                .nome(contabilidade.getNome())
                .ouathId(contabilidade.getOuathId())
                .salesForceId(contabilidade.getSalesForceId())
                .celular(contabilidade.getCelular())
                .parceiro(contabilidade.getParceiro())
                .urlParceiro(contabilidade.getUrlParceiro())
                .token(contabilidade.getToken())
            .build();
    }

    public static List<ContabilidadeDTO> fromEntities(List<Contabilidade> contabilidades) {
        return contabilidades.stream().map(contabilidade -> fromEntity(contabilidade)).collect(Collectors.toList());
    }

}
