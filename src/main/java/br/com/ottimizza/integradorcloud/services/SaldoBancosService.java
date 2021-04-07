package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.client.KafkaClient;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.PagRecRestantesDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.SaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.ViewSaldoBancosDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.SaldoBancosMapper;
import br.com.ottimizza.integradorcloud.domain.mappers.ViewSaldoBancosMapper;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import br.com.ottimizza.integradorcloud.domain.models.banco.Banco;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;
import br.com.ottimizza.integradorcloud.domain.models.banco.ViewSaldoBancos;
import br.com.ottimizza.integradorcloud.repositories.banco.BancoRepository;
import br.com.ottimizza.integradorcloud.repositories.livro_caixa.LivroCaixaRepository;
import br.com.ottimizza.integradorcloud.repositories.saldo_bancos.SaldoBancosRepository;

@Service
public class SaldoBancosService {
    
    @Inject
    SaldoBancosRepository repository;

    @Inject
    LivroCaixaRepository livroCaixaRepository;

    @Inject 
    BancoRepository bancoRepository;

    @Inject
    KafkaClient kafkaClient;

    public SaldoBancos salvaSaldo(SaldoBancosDTO saldoDTO) throws Exception {
        StringBuilder obj = new StringBuilder();
        int contador = 1;
        Banco banco = bancoRepository.findById(saldoDTO.getBancoId()).orElseThrow(() -> new NoResultException("Banco nao encontrado!"));
        SaldoBancos saldoBanco = repository.buscaPorBancoData(saldoDTO.getBancoId(), saldoDTO.getData());
        if(saldoBanco != null){
            saldoDTO.setId(saldoBanco.getId());
        }
        SaldoBancos retorno = repository.save(SaldoBancosMapper.fromDTO(saldoDTO));
        List<LivroCaixa> livrosCaixas = livroCaixaRepository.findByCnpjEmpresaBancoData(banco.getCnpjEmpresa(), banco.getId(), saldoDTO.getData().minusDays(1));
        int qntLivros = livrosCaixas.size();
		obj.append("[");
		for(LivroCaixa lc : livrosCaixas){
			if(contador == qntLivros){
				obj.append(lc.toString());
			}
			else{
				obj.append(lc.toString()+",");
			}
			contador ++;
		}
		obj.append("]");
		kafkaClient.integradaLivrosCaixas(obj.toString());
        return retorno;
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

    public SaldoBancosDTO buscaPorBancoData(BigInteger bancoId, String dataString) throws Exception {
        LocalDate data = LocalDate.of(Integer.parseInt(dataString.substring(0, 4)), Integer.parseInt(dataString.substring(5, 7)), Integer.parseInt(dataString.substring(8)));
        return SaldoBancosMapper.fromEntity(repository.buscaPorBancoData(bancoId, data));
    }

    public String deletaSaldo(BigInteger id) throws Exception {
        repository.deleteById(id);
        return "SaldoBanco removido com sucesso!";
    }

}
