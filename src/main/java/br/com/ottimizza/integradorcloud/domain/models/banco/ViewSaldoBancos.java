package br.com.ottimizza.integradorcloud.domain.models.banco;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "view_saldo_bancos")
@Data
@NoArgsConstructor
@Builder(toBuilder = true)
public class ViewSaldoBancos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    BigInteger id;

    @Column(name = "fk_banco_id")
    BigInteger banco;

    @Column(name = "data")
    LocalDate data;
    
    @Column(name = "saldo")
    Double saldo;
    
    @Column(name = "cnpj_empresa")
    String cnpjEmpresa;

    public ViewSaldoBancos(BigInteger id, BigInteger banco, LocalDate data, Double saldo, String cnpjEmpresa) {
        this.id = id;
        this.banco = banco;
        this.data = data;
        this.saldo = saldo;
        this.cnpjEmpresa = cnpjEmpresa;
    }

}
