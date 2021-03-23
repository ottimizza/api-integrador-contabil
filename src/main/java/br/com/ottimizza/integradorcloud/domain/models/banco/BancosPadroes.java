package br.com.ottimizza.integradorcloud.domain.models.banco;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "bancos_padroes")
public class BancosPadroes {
    
    @Id
	@SequenceGenerator(name = "banco_padrao_sequence", sequenceName = "banco_padrao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banco_padrao_sequence")
    private BigInteger id;

    @Column(length = 14)
	private String cnpjContabilidade;

	@Column(length = 14)
	private String cnpjEmpresa;
	
	private String nomeBanco;

    private String iconeBanco;

    private String codigoBanco;

    private String descricao;

    @Column(columnDefinition = "jsonb")
    private ObjetoAutenticacao objetoAutenticacao;

}
