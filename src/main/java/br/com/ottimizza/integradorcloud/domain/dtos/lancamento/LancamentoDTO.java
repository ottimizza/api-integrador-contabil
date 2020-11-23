package br.com.ottimizza.integradorcloud.domain.dtos.lancamento;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.arquivo.ArquivoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger id;

    private LocalDate dataMovimento;

    private String documento;

    private String descricao;

    private String portador;

    private String centroCusto;

    private ArquivoDTO arquivo;

    private String tipoPlanilha;

    private Short tipoLancamento;

    private String tipoMovimento; // CTB/CTBJUR/CTBPORTADOR

    private String contaMovimento;

    private String contaSugerida;

    private String contaContraPartida;

    private Short tipoConta;

    private Double valorOriginal;

    private Double valorPago;

    private Double valorJuros;

    private Double valorDesconto;

    private Double valorMulta;

    private String complemento01;

    private String complemento02;

    private String complemento03;

    private String complemento04;

    private String complemento05;

    private String cnpjEmpresa;

    private String cnpjContabilidade;

    private String idRoteiro;

    private String nomeArquivo;
    
    private Boolean ativo;
    
    private BigInteger accountingId;

    private BigInteger regraId;

    private String camposLancamento;
    
    private List<String> campos;

    public Lancamento patch(Lancamento lancamento) {
        if (contaMovimento != null && !contaMovimento.equals("")) {
            lancamento.setContaMovimento(contaMovimento);
        }
        if (contaContraPartida != null && !contaContraPartida.equals("")) {
            lancamento.setContaContraPartida(contaContraPartida);
        }
        if (tipoConta != null) {
            lancamento.setTipoConta(tipoConta);
        }
        if(ativo != null) {
        	lancamento.setAtivo(ativo);
        }

        return lancamento;
    }

    public static Pageable getPageRequest(PageCriteria searchCriteria) {
        return PageRequest.of(searchCriteria.getPageIndex(), searchCriteria.getPageSize(), getSort(searchCriteria));
    }

    public static Sort getSort(PageCriteria searchCriteria) {
        Sort sort = Sort.unsorted();
        if (searchCriteria.getSortOrder() != null && searchCriteria.getSortBy() != null) {
            sort = Sort.by(searchCriteria.getSortBy());
            if (searchCriteria.getSortOrder().equals(PageCriteria.Order.ASC)) {
                sort = sort.ascending();    
            } else if (searchCriteria.getSortOrder().equals(PageCriteria.Order.DESC)) {
                sort = sort.descending();
            }
        }

        return sort;
    }
    
}