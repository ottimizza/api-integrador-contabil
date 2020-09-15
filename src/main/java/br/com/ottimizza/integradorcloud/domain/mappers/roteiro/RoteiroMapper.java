package br.com.ottimizza.integradorcloud.domain.mappers.roteiro;

import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;

public class RoteiroMapper {

	public static Roteiro fromDTO(RoteiroDTO roteiro) {
		return Roteiro.builder()
				.id(roteiro.getId())
				.nome(roteiro.getNome())
				.urlArquivo(roteiro.getUrlArquivo())
				.cnpjEmpresa(roteiro.getCnpjEmpresa())
				.empresaId(roteiro.getEmpresaId())
				.cnpjContabilidade(roteiro.getCnpjContabilidade())
				.contabilidadeId(roteiro.getContabilidadeId())
				.tipoRoteiro(roteiro.getTipoRoteiro())
				.status(roteiro.getStatus())
				.mapeamento(roteiro.getMapeamento())
				.checklist(roteiro.getCheckList())
				.dataCriacao(roteiro.getDataCriacao())
				.dataAtualizacao(roteiro.getDataAtualizacao())
			.build();
	}
	
	public static RoteiroDTO fromEntity(Roteiro roteiro) {
		return RoteiroDTO.builder()
				.id(roteiro.getId())
				.nome(roteiro.getNome())
				.urlArquivo(roteiro.getUrlArquivo())
				.cnpjEmpresa(roteiro.getCnpjEmpresa())
				.empresaId(roteiro.getEmpresaId())
				.cnpjContabilidade(roteiro.getCnpjContabilidade())
				.contabilidadeId(roteiro.getContabilidadeId())
				.tipoRoteiro(roteiro.getTipoRoteiro())
				.status(roteiro.getStatus())
				.mapeamento(roteiro.getMapeamento())
				.checkList(roteiro.getChecklist())
				.dataCriacao(roteiro.getDataCriacao())
				.dataAtualizacao(roteiro.getDataAtualizacao())
			.build();
	}
	
}
