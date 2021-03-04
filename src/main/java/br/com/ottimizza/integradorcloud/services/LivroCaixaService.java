package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.client.StorageS3Client;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.mappers.LivroCaixaMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import br.com.ottimizza.integradorcloud.repositories.livro_caixa.LivroCaixaRepository;
import br.com.ottimizza.integradorcloud.utils.ServiceUtils;

@Service
public class LivroCaixaService {
	
	@Inject
	LivroCaixaRepository repository;
	
	@Inject
	OAuthClient oAuthClient; 
	
	@Inject
	StorageS3Client s3Client;
	
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

	public GrupoRegraDTO sugerirRegra(BigInteger livroCaixaId, String cnpjContabilidade, String cnpjEmpresa) throws Exception {
		try {
			GrupoRegra regraSugerida = repository.sugerirRegra(livroCaixaId, cnpjContabilidade, cnpjEmpresa);
			return GrupoRegraMapper.fromEntity(regraSugerida);
		}
		catch(Exception ex) {
			return null;
		}
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

	public LivroCaixaDTO buscaUltimoLancamentoContabilidadeEmpresa(String cnpjContabilidade, String cnpjEmpresa) {
		return LivroCaixaMapper.fromEntity(
				repository.findByCnpjContabilidadeAndCnpjEmpresaFirstByOrderByIdDesc(cnpjContabilidade, cnpjEmpresa)
				);
	}

//	public LivroCaixaDTO uploadFile(BigInteger idLivroCaixa, 
//									SalvaArquivoRequest salvaArquivo, 
//									MultipartFile arquivo, 
//									String authorization) {
//
//		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo.getCnpjEmpresa(), salvaArquivo.getCnpjContabilidade(), salvaArquivo.getApplicationId(), arquivo, authorization).getBody();
//		
//		LivroCaixa lc = repository.findById(idLivroCaixa).orElseThrow(() -> new NoResultException("livro caixa nao encontrado!"));
//		lc = lc.toBuilder().urlArquivo(S3_SERVICE_URL+"/resources/"+arquivoS3.getId().toString()+"/download").build();
//		
//		return LivroCaixaMapper.fromEntity(repository.save(lc));
//	}
	

}
