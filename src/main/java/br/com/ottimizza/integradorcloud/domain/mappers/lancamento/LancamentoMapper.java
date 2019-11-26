package br.com.ottimizza.integradorcloud.domain.mappers.lancamento;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

public class LancamentoMapper {

    public static LancamentoDTO toDto(Lancamento lancamento) {
        return new LancamentoDTO();
    }

    public static Lancamento fromDto(LancamentoDTO lancamento) {
        return new Lancamento();
    }

}