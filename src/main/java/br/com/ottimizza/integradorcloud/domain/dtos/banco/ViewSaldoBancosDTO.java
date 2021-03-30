package br.com.ottimizza.integradorcloud.domain.dtos.banco;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ViewSaldoBancosDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    BigInteger id;

    BigInteger banco;

    LocalDate data;
    
    Double saldo;
    
    String cnpjEmpresa;
    
    Double valores;
    
}
