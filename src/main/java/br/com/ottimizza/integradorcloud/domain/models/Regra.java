package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "regras")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class Regra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "regras_sequence", sequenceName = "regras_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regras_sequence")
    private BigInteger id;

    private String campo;

    private Short condicao;

    private String valor;

    @ManyToOne
    @JoinColumn(name = "fk_grupo_regras_id")
    private GrupoRegra grupoRegra;

    public static class Condicao {

        public static final int CONTEM = 1;

        public static final int NAO_CONTEM = 2;

        public static final int COMECAO_COM = 3;

        public static final int IGUAL = 4;

    }

}
