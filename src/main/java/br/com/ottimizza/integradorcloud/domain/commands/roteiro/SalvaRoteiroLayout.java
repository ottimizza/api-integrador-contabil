package br.com.ottimizza.integradorcloud.domain.commands.roteiro;

import java.util.List;

import br.com.ottimizza.integradorcloud.domain.dtos.LayoutPadraoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.RoteiroDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class SalvaRoteiroLayout {
    
    private RoteiroDTO roteiro;

    private List<LayoutPadraoDTO> layouts;

}
