package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.time.LocalDate;

import javax.inject.Inject;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.banco.BancoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.BancoMapper;
import br.com.ottimizza.integradorcloud.domain.models.banco.Banco;
import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;
import br.com.ottimizza.integradorcloud.repositories.banco.BancoRepository;
import br.com.ottimizza.integradorcloud.repositories.saldo_bancos.SaldoBancosRepository;

@Service
public class BancoService {

	@Inject
	OAuthClient oauthClient;
	
	@Inject
	BancoRepository bancoRepository;

	@Inject
	SaldoBancosRepository saldoBancosRepository;
	
	public BancoDTO salvar(BancoDTO bancoDto, OAuth2Authentication authentication) {
		BancoDTO banco = BancoMapper.fromEntity(bancoRepository.save(BancoMapper.fromDto(bancoDto)));
		saldoBancosRepository.save(SaldoBancos.builder()
										.bancoId(banco.getId())
										.data(LocalDate.of(2021, 01, 01))
										.saldo(0.0)
									.build());
		return banco;
	}

	public Page<Banco> buscarBancos(@Valid BancoDTO filter, 
									   @Valid PageCriteria pageCriteria,
									   OAuth2Authentication authentication) {
		return bancoRepository.buscaComFiltro(filter, pageCriteria);
	}

	public Page<BancosPadroes> buscaBancosPadroes(@Valid BancoDTO filter, 
									   			  @Valid PageCriteria pageCriteria) throws Exception {
		return bancoRepository.buscaBancosPadroesComFiltro(filter, pageCriteria);
    }


	public JSONObject remover(BigInteger id) throws Exception {
		JSONObject response = new JSONObject();
		try {
			bancoRepository.deleteById(id);
			response.put("status", "Success");
			response.put("message", "Excluído com sucesso!");
		} catch (Exception e) {
			response.put("status", "Error");
			response.put("message", "Houve um problema ao excluir!");
			return response;
		}
		return response;
	}
	
}