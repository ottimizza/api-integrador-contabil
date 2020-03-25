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
		return campo;
	}

}
