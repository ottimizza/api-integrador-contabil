package br.com.ottimizza.integradorcloud.domain.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.ottimizza.integradorcloud.domain.models.Regra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class GrupoRegraIgnoradaDTO {

	
	private BigInteger id;

    private Integer posicao;
    
    private String contaMovimento;

    private Short tipoLancamento;

    private String idRoteiro;

    private String cnpjEmpresa;

    private String cnpjContabilidade;
    
    private Integer contagemRegras;
    
    private Integer pesoRegras;

    private LocalDateTime dataCriacao;
    
    private LocalDateTime dataAtualizacao;

    private List<Regra> regras;

    private Boolean ativo;

    public String usuario;

    private List<String> camposRegras;

}
