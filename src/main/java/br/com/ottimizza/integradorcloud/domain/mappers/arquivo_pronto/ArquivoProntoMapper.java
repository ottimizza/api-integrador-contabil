package br.com.ottimizza.integradorcloud.domain.mappers.arquivo_pronto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.aquivo_pronto.ArquivoProntoDTO; 
import br.com.ottimizza.integradorcloud.domain.models.ArquivoPronto;

public class ArquivoProntoMapper {
	
	public static ArquivoPronto fromDTO(ArquivoProntoDTO arquivo) {
		return ArquivoPronto.builder()
				.id(arquivo.getId())
				.cnpjContabilidade(arquivo.getCnpjContabilidade())
				.cnpjEmpresa(arquivo.getCnpjEmpresa())
				.codigoHistorico(arquivo.getCodigoHistorico())
				.contaCredito(arquivo.getContaCredito())
				.contaDebito(arquivo.getContaDebito())
				.dataMovimento(arquivo.getDataMovimento())
				.lote(arquivo.getLote())
				.erpContabil(arquivo.getErpContabil())
				.tipoLancamento(arquivo.getTipoLancamento())
				.tipoMovimento(arquivo.getTipoMovimento())
			.build();
	}
	
	public static ArquivoProntoDTO fromEntity(ArquivoPronto arquivo) {
		return ArquivoProntoDTO.builder()
				.id(arquivo.getId())
				.cnpjContabilidade(arquivo.getCnpjContabilidade())
				.cnpjEmpresa(arquivo.getCnpjEmpresa())
				.codigoHistorico(arquivo.getCodigoHistorico())
				.contaCredito(arquivo.getContaCredito())
				.contaDebito(arquivo.getContaDebito())
				.dataMovimento(arquivo.getDataMovimento())
				.lote(arquivo.getLote())
				.erpContabil(arquivo.getErpContabil())
				.tipoLancamento(arquivo.getTipoLancamento())
				.tipoMovimento(arquivo.getTipoMovimento())
			.build();
	}
	
	public static List<ArquivoProntoDTO> fromEntities(List<ArquivoPronto> arquivos) {
        return arquivos.stream().map(arquivo -> fromEntity(arquivo)).collect(Collectors.toList());
    }

}
