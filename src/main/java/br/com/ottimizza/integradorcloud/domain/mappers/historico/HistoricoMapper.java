package br.com.ottimizza.integradorcloud.domain.mappers.historico;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfhistorico.SFHistorico;
import br.com.ottimizza.integradorcloud.domain.models.Historico;
import br.com.ottimizza.integradorcloud.utils.StringUtils;

public class HistoricoMapper { // @formatter:off

    public static Historico fromDto(HistoricoDTO historico) {
        return Historico.builder()
                .id(historico.getId())
                .contaMovimento(historico.getContaMovimento())
                .historico(historico.getHistorico())
                .cnpjEmpresa(historico.getCnpjEmpresa())
                .cnpjContabilidade(historico.getCnpjContabilidade())
                .dataCriacao(historico.getDataCriacao())
                .dataAtualizacao(historico.getDataAtualizacao())
            .build();    
    }

    public static HistoricoDTO fromEntity(Historico historico) {
        return HistoricoDTO.builder()
                .id(historico.getId())
                .contaMovimento(historico.getContaMovimento())
                .historico(historico.getHistorico())
                .cnpjEmpresa(historico.getCnpjEmpresa())
                .cnpjContabilidade(historico.getCnpjContabilidade())
                .dataCriacao(historico.getDataCriacao())
                .dataAtualizacao(historico.getDataAtualizacao())
            .build();
    }

    public static List<HistoricoDTO> fromEntities(List<Historico> historicos) {
        return historicos.stream().map(h -> fromEntity(h)).collect(Collectors.toList());
    }
    
    public static SFHistorico toSalesForce(HistoricoDTO historico) {
    	String codigoHistorico = "";
    	String texto01 = "";
    	
    	String campo01 = "";
    	String texto02 = "";
    	
    	String campo02 = "";
    	String texto03 = "";
    	
    	String campo03 = "";
    	String texto04 = "";
    	
    	String[] tudo = historico.getHistorico().split("\\$");
    	codigoHistorico = tudo[0].replace("CodigoHistorico:", "").trim();
    	texto01 = tudo[1].trim();
    	
    	campo01 = tudo[2].substring(tudo[2].indexOf("{")+1,tudo[2].indexOf("}"));
    	texto02 = tudo[2].substring(tudo[2].indexOf("}")+1);
    	
    	campo02 = tudo[3].substring(tudo[3].indexOf("{")+1,tudo[3].indexOf("}"));
    	texto03 = tudo[3].substring(tudo[3].indexOf("}")+1);
    	
    	campo03 = tudo[4].substring(tudo[4].indexOf("{")+1,tudo[4].indexOf("}"));
    	texto04 = tudo[4].substring(tudo[4].indexOf("}")+1);
    	
    	
    	return SFHistorico.builder()
    			.RecordTypeId(SFHistorico.RECORD_TYPE_ID)
    			.Roteiro__c(historico.getIdRoteiro())
    			.Codigo_Historico__c(codigoHistorico)
    			.Se_Campo__c("Conta Normal - Movimento - igual a ")
    			.O_texto_01__c(historico.getContaMovimento())
    			.Texto_01__c(texto01)
    			.Campo_01__c(StringUtils.trataCampoHistoricoProSalesForce(campo01))
    			.Texto_02__c(texto02)
    			.Campo_02__c(StringUtils.trataCampoHistoricoProSalesForce(campo02))
    			.Texto_03__c(texto03)
    			.Campo_03__c(StringUtils.trataCampoHistoricoProSalesForce(campo03))
    			.Texto_04__c(texto04)
    			.build();
    }
    

}