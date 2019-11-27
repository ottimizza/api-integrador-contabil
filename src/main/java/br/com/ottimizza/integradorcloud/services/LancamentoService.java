package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ottimizza.integradorcloud.domain.commands.lancamento.ImportacaoLancamentosRequest;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.lancamento.LancamentoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Arquivo;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import br.com.ottimizza.integradorcloud.repositories.arquivo.ArquivoRepository;
import br.com.ottimizza.integradorcloud.repositories.lancamento.LancamentoRepository;

@Service // @formatter:off
public class LancamentoService {

    @Inject
    LancamentoRepository lancamentoRepository;

    @Inject
    ArquivoRepository arquivoRepository;

    public List<LancamentoDTO> buscarTodos() throws Exception {
        return Arrays.asList(new LancamentoDTO[] {});
    }

    public LancamentoDTO buscarPorId(BigInteger id) throws Exception {
        return LancamentoMapper.fromEntity(lancamentoRepository.findById(id).orElseThrow(() -> new Exception("")));
    }

    public LancamentoDTO salvar(LancamentoDTO lancamentoDTO, Principal principal) throws Exception {
        Lancamento lancamento = LancamentoMapper.fromDto(lancamentoDTO);
        lancamento.setArquivo(arquivoRepository.save(lancamento.getArquivo()));
        validaLancamento(lancamento);
        return LancamentoMapper.fromEntity(lancamentoRepository.save(lancamento));
    }

    public LancamentoDTO salvar(BigInteger id, LancamentoDTO lancamentoDTO, Principal principal) throws Exception {
        Lancamento lancamento = lancamentoDTO.patch(
            lancamentoRepository.findById(id).orElseThrow(() -> new Exception(""))
        );
        validaLancamento(lancamento);
        return LancamentoMapper.fromEntity(lancamentoRepository.save(lancamento));
    }

    @Transactional(rollbackFor = Exception.class) 
    public List<LancamentoDTO> importar(ImportacaoLancamentosRequest importaLancamentos, Principal principal) throws Exception { 
        List<Lancamento> results = new ArrayList<>();
        Arquivo arquivo = arquivoRepository.save(
            Arquivo.builder()
                .nome(importaLancamentos.getArquivo().getNome())
                .cnpjContabilidade(importaLancamentos.getCnpjContabilidade())
                .cnpjEmpresa(importaLancamentos.getCnpjEmpresa()).build()
        );
        List<Lancamento> lancamentos = importaLancamentos.getLancamentos().stream().map((o) -> {
            return LancamentoMapper.fromDto(o).toBuilder()
                .arquivo(arquivo)
                .cnpjContabilidade(importaLancamentos.getCnpjContabilidade())
                .cnpjEmpresa(importaLancamentos.getCnpjEmpresa())
                .idRoteiro(importaLancamentos.getIdRoteiro()).build();
        }).collect(Collectors.toList());
        for (Lancamento lancamento : lancamentos) {
            validaLancamento(lancamento);
        }
        lancamentoRepository.saveAll(lancamentos).forEach(results::add);
        return LancamentoMapper.fromEntities(results);
    }

    private boolean validaLancamento(Lancamento lancamento) throws Exception {
        if (lancamento.getTipoLancamento() == null) {
            throw new IllegalArgumentException("Informe o tipo do lançamento!");
        }
        if (!Arrays.asList(Lancamento.Tipo.PAGAMENTO, Lancamento.Tipo.RECEBIMENTO).contains(lancamento.getTipoLancamento())) {
            throw new IllegalArgumentException("Informe um tipo de lançamento válido!");
        }
        if (lancamento.getCnpjContabilidade() == null || lancamento.getCnpjContabilidade().equals("")) {
            throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionada ao lançamento!");
        }
        if (lancamento.getCnpjEmpresa() == null || lancamento.getCnpjEmpresa().equals("")) {
            throw new IllegalArgumentException("Informe o cnpj da empresa relacionada ao lançamento!");
        }
        if (lancamento.getIdRoteiro() == null || lancamento.getIdRoteiro().equals("")) {
            throw new IllegalArgumentException("Informe o Id do Roteiro relacionado ao lançamento!");
        }
        if (lancamento.getTipoMovimento() == null || lancamento.getTipoMovimento().equals("")) {
            throw new IllegalArgumentException("Informe o tipo de movimento do lançamento!");
        }
        if (lancamento.getTipoPlanilha() == null || lancamento.getTipoPlanilha().equals("")) {
            throw new IllegalArgumentException("Informe o tipo da planilha!");
        }
        if (lancamento.getArquivo() == null || lancamento.getArquivo().getId() == null) {
            throw new IllegalArgumentException("Informe o arquivo relacionado ao lançamento!");
        }
        if (lancamento.getDataMovimento() == null) {
            throw new IllegalArgumentException("Informe a data do lançamento!");
        }
        if (lancamento.getDataMovimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data do lançamento não pode ser maior que hoje!");
        }
        if (lancamento.getValorOriginal() == null || lancamento.getValorOriginal() <= 0) {
            throw new IllegalArgumentException("Informe o valor do lançamento!");
        }
        return true;
    }

}