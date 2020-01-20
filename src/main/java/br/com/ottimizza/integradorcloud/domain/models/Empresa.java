package br.com.ottimizza.integradorcloud.domain.models;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "empresas")
@NoArgsConstructor
@AllArgsConstructor
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "empresas_sequence", sequenceName = "empresas_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresas_sequence")
    private BigInteger id;

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    private String razaoSocial;

    private String codigoERP;

    private BigInteger organizationId;

    private BigInteger accountingId;

}