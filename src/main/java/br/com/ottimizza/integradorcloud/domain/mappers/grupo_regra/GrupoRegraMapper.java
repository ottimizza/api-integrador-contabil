package br.com.ottimizza.integradorcloud.domain.mappers.grupo_regra;


import java.util.List;

import javax.inject.Inject;

import br.com.ottimizza.integradorcloud.domain.dtos.grupo_regra.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfparticularidade.SFParticularidade;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.Regra;
import br.com.ottimizza.integradorcloud.repositories.regra.RegraRepository;
import br.com.ottimizza.integradorcloud.utils.DateUtils;

public class GrupoRegraMapper {
	
	@Inject
	RegraRepository rr;

    public static GrupoRegra fromDto(GrupoRegraDTO grupoRegraDTO) {
        return GrupoRegra.builder()
                .id(grupoRegraDTO.getId())
                .posicao(grupoRegraDTO.getPosicao())
                .contaMovimento(grupoRegraDTO.getContaMovimento())
                .tipoLancamento(grupoRegraDTO.getTipoLancamento())
                .idRoteiro(grupoRegraDTO.getIdRoteiro())
                .cnpjEmpresa(grupoRegraDTO.getCnpjEmpresa())
                .contagemRegras(grupoRegraDTO.getContagemRegras())
                .dataCriacao(DateUtils.toDate(grupoRegraDTO.getDataCriacao()))
                .dataAtualizacao(DateUtils.toDate(grupoRegraDTO.getDataAtualizacao()))
                .cnpjContabilidade(grupoRegraDTO.getCnpjContabilidade())
				.ativo(grupoRegraDTO.getAtivo())
				.usuario(grupoRegraDTO.getUsuario())
				.camposRegras(grupoRegraDTO.getCamposRegras())
            .build();
    }

    public static GrupoRegraDTO fromEntity(GrupoRegra grupoRegra) {
        return GrupoRegraDTO.builder()
                .id(grupoRegra.getId())
                .posicao(grupoRegra.getPosicao())
                .contaMovimento(grupoRegra.getContaMovimento())
                .tipoLancamento(grupoRegra.getTipoLancamento())
                .idRoteiro(grupoRegra.getIdRoteiro())
                .cnpjEmpresa(grupoRegra.getCnpjEmpresa())
                .cnpjContabilidade(grupoRegra.getCnpjContabilidade())
                .contagemRegras(grupoRegra.getContagemRegras())
                .dataCriacao(DateUtils.toLocalDateTime(grupoRegra.getDataCriacao()))
                .dataAtualizacao(DateUtils.toLocalDateTime(grupoRegra.getDataAtualizacao()))
				.ativo(grupoRegra.getAtivo())
				.usuario(grupoRegra.getUsuario())
				.camposRegras(grupoRegra.getCamposRegras())
            .build();   
    }
    
    public static SFParticularidade toSalesForce(GrupoRegra grupoRegra, boolean comIdExterno) {
    	String sequenciaRegras = "500";
    	
    	List<Regra> regras = grupoRegra.getRegras();
    	String e01 = ""; 
    	String txt02 = "";	
    	
    	String e02 = ""; 
    	String txt03 = "";	
    	
    	String e03 = ""; 
    	String txt04 = "";	
    	
    	String e04 = ""; 
    	String txt05 = "";	
    	
    	if(regras.size() > 1) {
    		e01   = regras.get(1).getCampo();
    		txt02 = regras.get(1).getValor();
    		sequenciaRegras = "520";
    	}
    	if(regras.size() > 2) {
    		e02   = regras.get(2).getCampo();
    		txt03 = regras.get(2).getValor();
    		sequenciaRegras = "530";
    	}
    	if(regras.size() > 3 ) {
    		e03   = regras.get(3).getCampo();
    		txt04 =	regras.get(3).getValor();
    		sequenciaRegras = "540";
    	}
    	if(regras.size() > 4) {
    		e04   = regras.get(4).getCampo();
    		txt05 = regras.get(4).getValor();
    		sequenciaRegras = "550";
    	}
    	
    	SFParticularidade s = SFParticularidade.builder()
    			.RecordTypeId(SFParticularidade.RECORD_TYPE_ID)
    			.Roteiro__c(grupoRegra.getIdRoteiro())
    			.Conta_Movimento__c(grupoRegra.getContaMovimento())
    			.Se_Campo__c(regras.get(0).getCampo())
    			.O_texto_01__c(regras.get(0).getValor())
    			.E_01__c(e01)
    			.O_texto_02__c(txt02)
    			.E_02__c(e02)
    			.O_texto_03__c(txt03)
    			.E_03__c(e03)
    			.O_texto_04__c(txt04)
    			.E_04__c(e04)
    			.O_texto_05__c(txt05)
    			.Sequencia_das_Regras__c(sequenciaRegras)
    			.Ordem_OUD__c(grupoRegra.getPosicao())
    		.build();
    	
    	if(comIdExterno) s.setID_Externo__c(grupoRegra.getId().toString());
    	
    	return s;
    }
    
    public static SFParticularidade toSalesForce(GrupoRegra grupoRegra) {
    	return toSalesForce(grupoRegra, false);
    }

    public static SFParticularidade toSalesForce(GrupoRegra grupoRegra, List<Regra> regras, boolean comIdExterno) {
    	String sequenciaRegras = "500";
    	
    	String e01 = ""; 
    	String txt02 = "";	
    	
    	String e02 = ""; 
    	String txt03 = "";	
    	
    	String e03 = ""; 
    	String txt04 = "";	
    	
    	String e04 = ""; 
    	String txt05 = "";	
    	
    	if(regras.size() > 1) {
    		e01   = regras.get(1).getCampo();
    		txt02 = regras.get(1).getValor();
    		sequenciaRegras = "520";
    	}
    	if(regras.size() > 2) {
    		e02   = regras.get(2).getCampo();
    		txt03 = regras.get(2).getValor();
    		sequenciaRegras = "530";
    	}
    	if(regras.size() > 3 ) {
    		e03   = regras.get(3).getCampo();
    		txt04 =	regras.get(3).getValor(); 
    		sequenciaRegras = "540";
    	}
    	if(regras.size() > 4) {
    		e04   = regras.get(4).getCampo();
    		txt05 = regras.get(4).getValor();
    		sequenciaRegras = "550";
    	}
    	
    	SFParticularidade s = SFParticularidade.builder()
    			.RecordTypeId(SFParticularidade.RECORD_TYPE_ID)
    			.Roteiro__c(grupoRegra.getIdRoteiro())
    			.Conta_Movimento__c(grupoRegra.getContaMovimento())
    			.Se_Campo__c(regras.get(0).getCampo())
    			.O_texto_01__c(regras.get(0).getValor())
    			.E_01__c(e01)
    			.O_texto_02__c(txt02)
    			.E_02__c(e02)
    			.O_texto_03__c(txt03)
    			.E_03__c(e03)
    			.O_texto_04__c(txt04)
    			.E_04__c(e04)
    			.O_texto_05__c(txt05)
    			.Sequencia_das_Regras__c(sequenciaRegras)
    			.Ordem_OUD__c(grupoRegra.getPosicao())
    		.build();
    	
    	if(comIdExterno) s.setID_Externo__c(grupoRegra.getId().toString());
    	
    	return s;
    }
}