package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.BancoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Banco;

public class BancoMapper {
	public static Banco fromDto(BancoDTO dto) {
        return Banco.builder()
	                .id(dto.getId())
	                .cnpjContabilidade(dto.getCnpjContabilidade())
	                .cnpjEmpresa(dto.getCnpjEmpresa())
	            	.nomeBanco(dto.getNomeBanco())
	            	.descricao(dto.getDescricao())
	            	.build(); 
    }

    public static BancoDTO fromEntity(Banco entity) {
    	return BancoDTO.builder()
		                .id(entity.getId())
		                .cnpjContabilidade(entity.getCnpjContabilidade())
		                .cnpjEmpresa(entity.getCnpjEmpresa())
		            	.nomeBanco(entity.getNomeBanco())
		            	.descricao(entity.getDescricao())
		            	.build(); 
    }

    public static List<Banco> fromDto(List<BancoDTO> dtos) {
    	return dtos.stream().map(BancoMapper::fromDto).collect(Collectors.toList());
    }
        
    public static List<BancoDTO> fromEntity(List<Banco> entities) {
    	return entities.stream().map(BancoMapper::fromEntity).collect(Collectors.toList());
    }

}