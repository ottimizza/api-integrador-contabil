package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.banco.PagRecRestantesDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.SaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.ViewSaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.SaldoBancosMapper;
import br.com.ottimizza.integradorcloud.domain.mappers.ViewSaldoBancosMapper;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;
import br.com.ottimizza.integradorcloud.domain.models.banco.ViewSaldoBancos;
import br.com.ottimizza.integradorcloud.repositories.livro_caixa.LivroCaixaRepository;
import br.com.ottimizza.integradorcloud.repositories.saldo_bancos.SaldoBancosRepository;

@Service
public class SaldoBancosService {
    
    @Inject
    SaldoBancosRepository repository;

    @Inject
    LivroCaixaRepository livroCaixaRepository;

    public SaldoBancos salvaSaldo(SaldoBancosDTO saldoDTO) throws Exception {
        SaldoBancos saldoBanco = repository.buscaPorBancoData(saldoDTO.getBancoId(), saldoDTO.getData());
        if(saldoBanco != null){
            saldoDTO.setId(saldoBanco.getId());
        }
        return repository.save(SaldoBancosMapper.fromDTO(saldoDTO));
    }

    public List<ViewSaldoBancosDTO> buscaUltimoSaldo(String cnpjEmpresa) throws Exception {
        List<ViewSaldoBancosDTO> saldos = ViewSaldoBancosMapper.fromEntities(repository.buscaUltimoSaldoPorBancos(cnpjEmpresa));
        for(ViewSaldoBancosDTO saldo : saldos) {
            Double valores = repository.contarValoresBanco(saldo.getCnpjEmpresa().trim(), saldo.getData(), saldo.getBanco());
            saldo.setValores(valores);
        }
        return saldos;
    }

    public PagRecRestantesDTO contarPagamentosRecebimentosRestantes(String cnpjEmpresa) throws Exception {
        return PagRecRestantesDTO.builder()
                .pagamentosRestantes(livroCaixaRepository.buscaPagamentosPendentes(cnpjEmpresa, LocalDate.now()))
                .recebimentosRestantes(livroCaixaRepository.buscaRecebimentosPendentes(cnpjEmpresa, LocalDate.now()))
            .build();
    }

    public String deletaSaldo(BigInteger id) throws Exception {
        repository.deleteById(id);
        return "SaldoBanco removido com sucesso!";
    }

}