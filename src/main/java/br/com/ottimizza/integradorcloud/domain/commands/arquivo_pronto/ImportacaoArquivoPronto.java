package br.com.ottimizza.integradorcloud.domain.commands.arquivo_pronto;

import java.util.List;

import br.com.ottimizza.integradorcloud.domain.dtos.aquivo_pronto.ArquivoProntoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ImportacaoArquivoPronto {

	private String cnpjContabilidade;

	private String cnpjEmpresa;
	
	List<ArquivoProntoDTO> arquivos;
	
}
