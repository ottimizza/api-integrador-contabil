package br.com.ottimizza.integradorcloud.domain.mappers;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public class LivroCaixaMapper {

	
	public static LivroCaixaDTO fromEntity(LivroCaixa livroCaixa) {
		return LivroCaixaDTO.builder()
				.id(livroCaixa.getId())
				.cnpjContabilidade(livroCaixa.getCnpjContabilidade())
				.cnpjEmpresa(livroCaixa.getCnpjEmpresa())
				.complemento(livroCaixa.getComplemento())
				.dataMovimento(livroCaixa.getDataMovimento())
				.dataPrevisaoPagamento(livroCaixa.getDataPrevisaoPagamento())
				.valorOriginal(livroCaixa.getValorOriginal())
				.valorPago(livroCaixa.getValorPago())
				.valorFinal(livroCaixa.getValorFinal())
				.descricao(livroCaixa.getDescricao())
				.bancoId(livroCaixa.getBancoId())
				.categoriaId(livroCaixa.getCategoriaId())
				.tipoMovimento(livroCaixa.getTipoMovimento())
				.complemento(livroCaixa.getComplemento())
				.linkArquivo(livroCaixa.getLinkArquivo())
				.origem(livroCaixa.getOrigem())
				.integradoContabilidade(livroCaixa.getIntegradoContabilidade())
				.status(livroCaixa.getStatus())
				.textoDocumento(livroCaixa.getTextoDocumento())
				.termos(livroCaixa.getTermos())
				.contaMovimento(livroCaixa.getContaMovimento())
				.criadoPor(livroCaixa.getCriadoPor())
				.dataCriacao(livroCaixa.getDataCriacao())
				.dataAtualizacao(livroCaixa.getDataAtualizacao())
			.build();
	}
	
	
	public static LivroCaixa fromDTO (LivroCaixaDTO livroCaixa) {
		return LivroCaixa.builder()
				.id(livroCaixa.getId())
				.cnpjContabilidade(livroCaixa.getCnpjContabilidade())
				.cnpjEmpresa(livroCaixa.getCnpjEmpresa())
				.complemento(livroCaixa.getComplemento())
				.dataMovimento(livroCaixa.getDataMovimento())
				.dataPrevisaoPagamento(livroCaixa.getDataPrevisaoPagamento())
				.valorOriginal(livroCaixa.getValorOriginal())
				.valorPago(livroCaixa.getValorPago())
				.valorFinal(livroCaixa.getValorFinal())
				.descricao(livroCaixa.getDescricao())
				.bancoId(livroCaixa.getBancoId())
				.categoriaId(livroCaixa.getCategoriaId())
				.tipoMovimento(livroCaixa.getTipoMovimento())
				.complemento(livroCaixa.getComplemento())
				.linkArquivo(livroCaixa.getLinkArquivo())
				.origem(livroCaixa.getOrigem())
				.integradoContabilidade(livroCaixa.getIntegradoContabilidade())
				.status(livroCaixa.getStatus())
				.textoDocumento(livroCaixa.getTextoDocumento())
				.termos(livroCaixa.getTermos())
				.contaMovimento(livroCaixa.getContaMovimento())
				.criadoPor(livroCaixa.getCriadoPor())
				.dataCriacao(livroCaixa.getDataCriacao())
				.dataAtualizacao(livroCaixa.getDataAtualizacao())
			.build();
	}
	
	public static List<LivroCaixaDTO> fromEntities(List<LivroCaixa> livroCaixas) {
        return livroCaixas.stream().map(livro -> fromEntity(livro)).collect(Collectors.toList());
    }
}
