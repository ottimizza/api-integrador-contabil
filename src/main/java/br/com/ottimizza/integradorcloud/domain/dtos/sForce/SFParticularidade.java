package br.com.ottimizza.integradorcloud.domain.dtos.sForce;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SFParticularidade implements Serializable{

    public static final String S_NAME = "Roteiros_vs_Contas__c";

    public static final String RECORD_TYPE_ID = "01215000001Ngz1";

    private static final long serialVersionUID = 1L;

    @JsonProperty(value="ID_Externo__c") // GrupoRegra.id
    private String ID_Externo__c;

    @JsonProperty(value="RecordTypeId") 
    private String RecordTypeId; // RECORD_TYPE_ID

    @JsonProperty(value="Roteiro__c") // GrupoRegra.roteiroId
    private String Roteiro__c;

    @JsonProperty(value="Conta_Movimento__c")
    private String Conta_Movimento__c;

    @JsonProperty(value="Sequencia_das_Regras__c")
    private String Sequencia_das_Regras__c;
    
    @JsonProperty(value="Ordem_OUD__c")
    private Integer Ordem_OUD__c;
    
    // Regras
    // 01
    @JsonProperty(value="Se_Campo__c") 
    private String Se_Campo__c;
    
    @JsonProperty(value="O_texto_01__c") 
    private String O_texto_01__c;

    // 02
    @JsonProperty(value="E_01__c") 
    private String E_01__c;

    @JsonProperty(value="O_texto_02__c") 
    private String O_texto_02__c;
    
    // 03
    @JsonProperty(value="E_02__c") 
    private String E_02__c;
    
    @JsonProperty(value="O_texto_03__c") 
    private String O_texto_03__c;
    
    // 04
    @JsonProperty(value="E_03__c") 
    private String E_03__c;

    @JsonProperty(value="O_texto_04__c") 
    private String O_texto_04__c;
    
    // 05
    @JsonProperty(value="E_04__c") 
    private String E_04__c;

    @JsonProperty(value="O_texto_05__c") 
    private String O_texto_05__c;
    
    @JsonProperty(value="Conta_Portador__c")
    private String Conta_Portador__c;
    
    @JsonProperty(value="Conta_Desconto__c")
    private String Conta_Desconto__c;

    @JsonProperty(value="Conta_Juros__c")
    private String Conta_Juros__c;

    @JsonProperty(value="Conta_Multa__c")
    private String Conta_Multa__c;

    @JsonProperty(value="Inverte_Contas__c")
    private String Inverte_Contas__c;

}
