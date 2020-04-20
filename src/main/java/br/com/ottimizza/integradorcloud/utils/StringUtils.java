package br.com.ottimizza.integradorcloud.utils;

public class StringUtils {

	
	
	public static String trataProSalesForce(String campo) {
		if (campo.contains("complemento")) {
			campo = campo.replaceFirst("c", "C").replace("o0", "o (0").concat(") contém");
		}
		if (campo.contains("tipoPlanilha")) {
			campo = "Tipo Planilha contém";
		}
		if (campo.contains("portador")) {
			campo = "Portador contém";
		}
		if (campo.contains("descricao")) {
			campo = "Fornecedor/Cliente contém";
		}
		if (campo.contains("documento")) {
			campo = "Documento/NF contém";
		}
		if(campo.contains("nomeArquivo")) {
			campo = "Nome do Arquivo contém";
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
			campo = "Mes-Ano Atual";
		}
		if(campo.contains("competenciaAnterior")) {
			campo = "Mes-Ano Anterior";
		}
		if(campo.contains("documento")) {
			campo = "Documento/NF";
		}
		if (campo.contains("complemento")) {
			campo = campo.replaceFirst("c", "C").replace("o0", "o (0").concat(")");
		}
		if(campo.contains("nenhum")) {
			campo = "-- Nenhum --";
		}
		return campo;
	}

}
