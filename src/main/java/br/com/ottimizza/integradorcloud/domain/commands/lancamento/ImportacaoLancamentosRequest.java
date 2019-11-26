package br.com.ottimizza.integradorcloud.domain.commands.lancamento;

import java.io.Serializable;
import java.util.List;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Arquivo;
import lombok.Data;

@Data
public class ImportacaoLancamentosRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cnpjContabilidade;

    private String cnpjEmpresa;

    private String idRoteiro;

    private Arquivo arquivo;

    private List<LancamentoDTO> lancamentos;

}