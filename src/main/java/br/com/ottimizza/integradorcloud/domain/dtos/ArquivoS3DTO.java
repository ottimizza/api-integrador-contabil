package br.com.ottimizza.integradorcloud.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class ArquivoS3DTO {

    private BigInteger id;

    private BigInteger contabilidadeId;

    private BigInteger empresaId;

    private Boolean processado;

}
