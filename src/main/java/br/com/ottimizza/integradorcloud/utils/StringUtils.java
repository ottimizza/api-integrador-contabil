package br.com.ottimizza.integradorcloud.utils;

public class StringUtils {
	
	public static String trataProSalesForce(String campo, Short condicao) {
		
		if (campo.contains("complemento")) {
			campo = campo.replaceFirst("c", "C").replace("o0", "o (0").concat(")");
		}
		else if (campo.contains("tipoPlanilha")) {
			campo = "Tipo Planilha";
		}
		else if (campo.contains("portador")) {
			campo = "Portador";
		}
		else if (campo.contains("descricao")) {
			campo = "Fornecedor/Cliente";
		}
		else if (campo.contains("documento")) {
			campo = "Documento/NF";
		}
		else if(campo.contains("nomeArquivo")) {
			campo = "Nome do Arquivo";
		}
		
		//TRATANDO CONDICAO 
		
		if(condicao == 1) {
			campo = campo.concat(" contém");
		}
		else if(condicao == 2) {
			campo = campo.concat(" não contém");
		}
		else if(condicao == 3) {
			campo = campo.concat(" começa com");
		}
		else if(condicao == 4) {
			campo = campo.concat(" igual a");
		}
		return campo;
		
	}
	
	public static String trataCampoHistoricoProSalesForce(String campo) {
		if(campo.contains("descricao")) {
			campo = "Fornecedor/Cliente";
		}
		if(campo.contains("portador")) {
			campo = "Portador";
		}
		if(campo.contains("competencia")) {
			if(campo.contains("Anterior"))
				campo = "Mes-Ano Anterior";
			else
				campo = "Mes-Ano Atual";
		}
		if(campo.contains("documento")) {
			campo = "Documento/NF";
		}
		if (campo.contains("complemento")) {
			campo = campo.replaceFirst("c", "C").replace("o0", "o (0").concat(")");
		}
		if(campo.contains("nenhum")) {
			campo = "";
		}
		return campo;
	}

}
