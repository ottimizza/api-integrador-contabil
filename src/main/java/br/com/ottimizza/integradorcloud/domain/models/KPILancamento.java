package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KPILancamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer total;

    private Integer fornecedorCliente;

    private Integer outrasContas;

    private Integer ignorar;

    private Integer pular;

}