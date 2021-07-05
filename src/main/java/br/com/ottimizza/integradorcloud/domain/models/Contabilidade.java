package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;

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
@Table(name = "contabilidades")
@Builder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class Contabilidade implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "contabilidades_sequence", sequenceName = "contabilidades_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contabilidades_sequence")
	private BigInteger id;
	
	private String cnpj;
	
	private String nome;
	
	private String salesForceId;
	
	private BigInteger ouathId;

	private Short parceiro;

	private String urlParceiro;

	private String token;

	private String celular;

	public static class Parceiro { 
        public static final Short TOUSCHAT = 0;
        public static final Short DIGISAC = 1;
		public static final Short NENHUM = 99;
    }

	@PrePersist @PreUpdate
    public void prePersist() {
    	if(this.parceiro == null)
			this.parceiro = Parceiro.NENHUM;
    }

}
