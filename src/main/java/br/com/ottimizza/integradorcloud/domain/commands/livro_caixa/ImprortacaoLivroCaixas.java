package br.com.ottimizza.integradorcloud.domain.commands.livro_caixa;

import java.util.List;

import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaImportadoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ImprortacaoLivroCaixas {
    
    private String cnpjEmpresa;

    private String banco;

    private List<LivroCaixaImportadoDTO> livrosCaixas;

}
