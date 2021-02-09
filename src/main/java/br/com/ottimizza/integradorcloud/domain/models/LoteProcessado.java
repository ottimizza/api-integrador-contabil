package br.com.ottimizza.integradorcloud.domain.models;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "lotes_processados")
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class LoteProcessado { 
	
	@Id
	@Column(name = "id", nullable = false)
    @SequenceGenerator(name = "lotes_processados_sequence", sequenceName = "lotes_processados_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lotes_processados_sequence")
	private BigInteger id;
	
	@Column(name = "cnpj_contabilidade")
	private String cnpjContabilidade;
	
	@Column(name = "contabilidade")
	private String contabilidade;
	
	@Column(name = "cnpj_empresa")
	private String cnpjEmpresa;
	
	@Column(name = "empresa")
	private String empresa;
	
	@Column(name = "codigo_erp")
	private String codigoErp;
	
	@Column(name = "erp_contabil")
	private String erpContabil;
	
	@Column(name = "lote")
	private String lote;
	
	@Column(name = "quantidade_lancamentos")
	private Long quantidadeLancamentos; 
	
	@Column(name = "tipo_registro")
	private String tipoRegistro;
	
	@Column(name = "ano_mes")
	private LocalDate anoMes;
	
	@Column(name = "data_criacao")
	private LocalDateTime dataCriacao;
	
	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;
	
	@PrePersist
    @PreUpdate
    public void preUpdate() {
        if (dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
        this.dataAtualizacao = LocalDateTime.now();
    }

}
