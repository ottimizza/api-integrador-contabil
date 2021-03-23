package br.com.ottimizza.integradorcloud.domain.commands.lote_processado;

import java.util.List;

import br.com.ottimizza.integradorcloud.domain.dtos.LoteProcessadoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ImportacaoLoteProcessado {

	private String cnpjContabilidade;

	private String cnpjEmpresa;

	List<LoteProcessadoDTO> lotes;

}
