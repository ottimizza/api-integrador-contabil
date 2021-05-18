package br.com.ottimizza.integradorcloud.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Mapeamento;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class RoteiroDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private BigInteger id;
	
	private String nome;
	
	private String urlArquivo;
	
	private BigInteger empresaId;
	
	private String cnpjEmpresa;
	
	private BigInteger contabilidadeId;
	
	private String cnpjContabilidade;
	
	private String tipoRoteiro;
	
	private Short status;
	
	private Mapeamento mapeamento;
	
	private Boolean checkList;
	
	private String usuario;
	
	private String layoutsPadroes;

	private Short tipoProjeto;
	
	private LocalDateTime dataCriacao;
	
	private LocalDateTime dataAtualizacao;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String nomeCompleto;
	
	public Roteiro patch(Roteiro roteiro) {
		
		if(nome != null  && !nome.equals(""))
			roteiro.setNome(nome);
		
		if(urlArquivo != null && !urlArquivo.equals(""))
			roteiro.setUrlArquivo(urlArquivo);
		
		if(status != null)
			roteiro.setStatus(status);
		
		if(mapeamento != null)
			roteiro.setMapeamento(mapeamento);
		
		if(tipoRoteiro != null)
			roteiro.setTipoRoteiro(tipoRoteiro);
		
		if(checkList != null)
			roteiro.setChecklist(checkList);
		
		if(usuario != null && !usuario.equals(""))
			roteiro.setUsuario(usuario);
		
		if(tipoProjeto != null && !tipoProjeto.equals(""))
			roteiro.setTipoProjeto(tipoProjeto);
			
		if(layoutsPadroes != null && !layoutsPadroes.equals(""))
			roteiro.setLayoutsPadroes(layoutsPadroes);
			
		return roteiro;
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
