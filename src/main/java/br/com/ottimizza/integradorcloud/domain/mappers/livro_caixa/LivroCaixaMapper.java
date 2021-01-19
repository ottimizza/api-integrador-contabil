package br.com.ottimizza.integradorcloud.domain.mappers.livro_caixa;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.livro_caixa.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public class LivroCaixaMapper {

	
	public static LivroCaixaDTO fromEntity(LivroCaixa livroCaixa) {
		return LivroCaixaDTO.builder()
				.id(livroCaixa.getId())
				.cnpjContabilidade(livroCaixa.getCnpjContabilidade())
				.cnpjEmpresa(livroCaixa.getCnpjEmpresa())
				.complemento(livroCaixa.getComplemento())
				.dataMovimento(livroCaixa.getDataMovimento())
				.fornecerdor(livroCaixa.getFornecerdor())
				.valorEntrada(livroCaixa.getValorEntrada())
				.valorSaida(livroCaixa.getValorSaida())
				.banco(livroCaixa.getBanco())
			.build();
	}
	
	
	public static LivroCaixa fromDTO (LivroCaixaDTO livroCaixa) {
		return LivroCaixa.builder()
				.id(livroCaixa.getId())
				.cnpjContabilidade(livroCaixa.getCnpjContabilidade())
				.cnpjEmpresa(livroCaixa.getCnpjEmpresa())
				.complemento(livroCaixa.getComplemento())
				.dataMovimento(livroCaixa.getDataMovimento())
				.fornecerdor(livroCaixa.getFornecerdor())
				.valorEntrada(livroCaixa.getValorEntrada())
				.valorSaida(livroCaixa.getValorSaida())
				.banco(livroCaixa.getBanco())
			.build();
	}
	
	public static List<LivroCaixaDTO> fromEntities(List<LivroCaixa> livroCaixas) {
        return livroCaixas.stream().map(livro -> fromEntity(livro)).collect(Collectors.toList());
    }
}
