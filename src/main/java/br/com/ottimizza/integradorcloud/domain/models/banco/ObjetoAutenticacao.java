package br.com.ottimizza.integradorcloud.domain.models.banco;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
public class ObjetoAutenticacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String callBack;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String metodo;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientSecret;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

}