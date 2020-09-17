package br.com.ottimizza.integradorcloud.domain.models.roteiro;

public enum StatusRoteiro {

	INICIANDO(1),
	PROCESSANDO_PLANILHA(2),
	ARQUIVO_OK(3),
	AGUARDANDO_MAPEAMENTO(4),
	AGUARDANDO_DETALHAMENTO(5),
	AGUARDANDO_CONFIRMACAO(6),
	PRE_ENTREGA(7),
	OK(8);
	
	public int statusRoteiro;
	StatusRoteiro(int i) {
		statusRoteiro = i;	
	}
}
