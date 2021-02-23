package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.BuscaPorCnpjsDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.livro_caixa.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.user.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.livro_caixa.LivroCaixaMapper;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import br.com.ottimizza.integradorcloud.repositories.livro_caixa.LivroCaixaRepository;
import br.com.ottimizza.integradorcloud.utils.ServiceUtils;

@Service
public class LivroCaixaService {
	
	@Inject
	LivroCaixaRepository repository;
	
	@Inject
	OAuthClient oAuthClient; 
	
	public LivroCaixaDTO salva(LivroCaixaDTO livroCaixa) throws Exception {
		LivroCaixa retorno = repository.save(LivroCaixaMapper.fromDTO(livroCaixa));
		return LivroCaixaMapper.fromEntity(retorno);
	}
	
	public LivroCaixaDTO patch(BigInteger id, LivroCaixaDTO livroCaixaDTO) throws Exception {
		LivroCaixa livroCaixa = repository.findById(id).orElseThrow(() -> new NoResultException("Livro Caixa nao encontrado!"));
		LivroCaixa retorno = livroCaixaDTO.patch(livroCaixa);
		return LivroCaixaMapper.fromEntity(retorno);
	}
	
	public Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria) throws Exception {
		return repository.buscaComFiltro(filtro, criteria);
	}
	
	public String deletaPorId(BigInteger id) throws Exception {
		repository.deleteById(id);
		return "Livro Caixa removido com sucesso!";
	}

	public JSONObject deletaNaoIntegrado(BigInteger id) {
		JSONObject response = new JSONObject();
		LivroCaixa livroCaixa = repository.findById(id).orElse(null);
		if (!livroCaixa.getIntegradoContabilidade()) {
			try {
				repository.deleteById(id);
				response.put("status", "Success");
				response.put("message", "Excluído com sucesso!");
			} catch (Exception e) {
				response.put("status", "Error");
				response.put("message", "Houve um problema ao excluir!");
				return response;
			}
		} else {
			response.put("status", "Unauthorized");
			response.put("message", "Lançamento ja integrado não pode ser excluido!");
			return response;
		}
		return response;
	}

	public LivroCaixaDTO clonarLivroCaixa(BigInteger id, OAuth2Authentication authentication) {
		LivroCaixa livroCaixa = repository.findById(id).orElse(null);
		if (livroCaixa == null) return null;

		UserDTO userInfo = oAuthClient.getUserInfo(ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
		LivroCaixa novo	 = new LivroCaixa(livroCaixa);
		novo.setCriadoPor(userInfo.getFirstName());
		return LivroCaixaMapper.fromEntity(repository.save(novo));
	
	}

	public LivroCaixaDTO buscaUltimoLancamentoContabilidadeEmpresa(BuscaPorCnpjsDTO filtro) {
		return LivroCaixaMapper.fromEntity(
				repository.findByCnpjContabilidadeAndCnpjEmpresaFirstByOrderByIdDesc(filtro.getCnpjContabilidade(), filtro.getCnpjEmpresa())
				);
	}
	
	

}
