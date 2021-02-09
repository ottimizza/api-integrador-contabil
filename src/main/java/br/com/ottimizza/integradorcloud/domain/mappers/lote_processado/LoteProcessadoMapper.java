package br.com.ottimizza.integradorcloud.domain.mappers.lote_processado;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.lote_processado.LoteProcessadoDTO;
import br.com.ottimizza.integradorcloud.domain.models.LoteProcessado;

public class LoteProcessadoMapper {

	public static LoteProcessado fromDTO(LoteProcessadoDTO lote) {
		return LoteProcessado.builder()
				.id(lote.getId())
				.cnpjContabilidade(lote.getCnpjContabilidade())
				.contabilidade(lote.getContabilidade())
				.cnpjEmpresa(lote.getCnpjEmpresa())
				.empresa(lote.getEmpresa())
				.codigoErp(lote.getCodigoErp())
				.erpContabil(lote.getErpContabil())
				.lote(lote.getLote())
				.quantidadeLancamentos(lote.getQuantidadeLancamentos())
				.tipoRegistro(lote.getTipoRegistro())
				.anoMes(lote.getAnoMes())
				.dataCriacao(lote.getDataCriacao())
				.dataAtualizacao(lote.getDataAtualizacao())
			.build();
	}
	
	public static LoteProcessadoDTO fromEntity(LoteProcessado lote) {
		return LoteProcessadoDTO.builder()
				.id(lote.getId())
				.cnpjContabilidade(lote.getCnpjContabilidade())
				.contabilidade(lote.getContabilidade())
				.cnpjEmpresa(lote.getCnpjEmpresa())
				.empresa(lote.getEmpresa())
				.codigoErp(lote.getCodigoErp())
				.erpContabil(lote.getErpContabil())
				.lote(lote.getLote())
				.quantidadeLancamentos(lote.getQuantidadeLancamentos())
				.tipoRegistro(lote.getTipoRegistro())
				.anoMes(lote.getAnoMes())
				.dataCriacao(lote.getDataCriacao())
				.dataAtualizacao(lote.getDataAtualizacao())
			.build();
	}
	
	public static List<LoteProcessadoDTO> fromEntities(List<LoteProcessado> lotes) {
        return lotes.stream().map(lote -> fromEntity(lote)).collect(Collectors.toList());
    }
}
