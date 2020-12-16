package br.com.ottimizza.integradorcloud.domain.mappers.lancamento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.ArquivoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

public class LancamentoMapper { // @formatter:off

    public static Lancamento fromDto(LancamentoDTO lancamento) {
        return Lancamento.builder()
            .id(lancamento.getId())
            .dataMovimento(lancamento.getDataMovimento())
            .documento(lancamento.getDocumento())
            .descricao(lancamento.getDescricao())
            .portador(lancamento.getPortador())
            .centroCusto(lancamento.getCentroCusto())
            .arquivo(
                lancamento.getArquivo() != null ? ArquivoMapper.fromDto(lancamento.getArquivo()) : null
            )
            .tipoPlanilha(lancamento.getTipoPlanilha())
            .tipoLancamento(lancamento.getTipoLancamento())
            .tipoMovimento(lancamento.getTipoMovimento()) 
            .contaMovimento(lancamento.getContaMovimento())
            .contaContraPartida(lancamento.getContaContraPartida())
            .contaSugerida(lancamento.getContaSugerida())
            .tipoConta(lancamento.getTipoConta())
            .valorOriginal(lancamento.getValorOriginal())
            .valorPago(lancamento.getValorPago())
            .valorJuros(lancamento.getValorJuros())
            .valorDesconto(lancamento.getValorDesconto())
            .valorMulta(lancamento.getValorMulta())
            .complemento01(lancamento.getComplemento01())
            .complemento02(lancamento.getComplemento02())
            .complemento03(lancamento.getComplemento03())
            .complemento04(lancamento.getComplemento04())
            .complemento05(lancamento.getComplemento05())
            .cnpjEmpresa(lancamento.getCnpjEmpresa())
            .cnpjContabilidade(lancamento.getCnpjContabilidade())
            .idRoteiro(lancamento.getIdRoteiro())
            .nomeArquivo(lancamento.getNomeArquivo())
            .ativo(lancamento.getAtivo())
            .accountingId(lancamento.getAccountingId())
            .regraId(lancamento.getRegraId())
            .campos(lancamento.getCampos())
        .build();
    }

    public static LancamentoDTO fromEntity(Lancamento lancamento) {
        return LancamentoDTO.builder()
                .id(lancamento.getId())
                .dataMovimento(lancamento.getDataMovimento())
                .documento(lancamento.getDocumento())
                .descricao(lancamento.getDescricao())
                .portador(lancamento.getPortador())
                .centroCusto(lancamento.getCentroCusto())
                .arquivo(
                    lancamento.getArquivo() != null ? ArquivoMapper.fromEntity(lancamento.getArquivo()) : null
                )
                .tipoPlanilha(lancamento.getTipoPlanilha())
                .tipoLancamento(lancamento.getTipoLancamento())
                .tipoMovimento(lancamento.getTipoMovimento()) 
                .contaMovimento(lancamento.getContaMovimento())
                .contaContraPartida(lancamento.getContaContraPartida())
                .contaSugerida(lancamento.getContaSugerida())
                .tipoConta(lancamento.getTipoConta())
                .valorOriginal(lancamento.getValorOriginal())
                .valorPago(lancamento.getValorPago())
                .valorJuros(lancamento.getValorJuros())
                .valorDesconto(lancamento.getValorDesconto())
                .valorMulta(lancamento.getValorMulta())
                .complemento01(lancamento.getComplemento01())
                .complemento02(lancamento.getComplemento02())
                .complemento03(lancamento.getComplemento03())
                .complemento04(lancamento.getComplemento04())
                .complemento05(lancamento.getComplemento05())
                .cnpjEmpresa(lancamento.getCnpjEmpresa())
                .cnpjContabilidade(lancamento.getCnpjContabilidade())
                .idRoteiro(lancamento.getIdRoteiro())
                .nomeArquivo(lancamento.getNomeArquivo())
                .ativo(lancamento.getAtivo())
                .accountingId(lancamento.getAccountingId())
                .regraId(lancamento.getRegraId())
                .campos(lancamento.getCampos())
            .build();
    }

    public static List<LancamentoDTO> fromEntities(List<Lancamento> lancamentos) {
        return lancamentos.stream().map(user -> fromEntity(user)).collect(Collectors.toList());
    }

}