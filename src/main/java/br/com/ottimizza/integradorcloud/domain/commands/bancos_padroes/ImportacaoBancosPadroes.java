package br.com.ottimizza.integradorcloud.domain.commands.bancos_padroes;

import java.util.List;

import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ImportacaoBancosPadroes {
    
    private List<BancosPadroes> bancos;
}
