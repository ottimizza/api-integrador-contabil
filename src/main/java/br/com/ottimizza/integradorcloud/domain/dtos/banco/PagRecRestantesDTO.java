package br.com.ottimizza.integradorcloud.domain.dtos.banco;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class PagRecRestantesDTO {

    private Double pagamentosRestantes;

    private Double recebimentosRestantes;

}