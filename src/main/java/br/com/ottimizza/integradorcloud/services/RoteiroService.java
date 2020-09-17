package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.ArquivoS3DTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.client.StorageS3Client;
import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sfempresa.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.dtos.user.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.roteiro.RoteiroMapper;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;
import br.com.ottimizza.integradorcloud.repositories.empresa.EmpresaRepository;
import br.com.ottimizza.integradorcloud.repositories.roteiro.RoteiroRepository;

@Service
public class RoteiroService {

	@Inject
	RoteiroRepository repository;
	
	@Inject
	EmpresaRepository empresaRepository;
	
	@Inject
	StorageS3Client s3Client;
	
	@Inject
	OAuthClient oauthClient;
	
	@Inject
	SalesForceClient sfClient;
	
	@Value("${storage-s3.service.url}")
    private String S3_SERVICE_URL;
	
	public RoteiroDTO salva(RoteiroDTO roteiroDTO, OAuth2Authentication authentication) throws Exception {
		UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody().getRecord();
		roteiroDTO.setUsuario(userInfo.getUsername());
		
		Roteiro roteiro = RoteiroMapper.fromDTO(roteiroDTO);
		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	public RoteiroDTO uploadPlanilha(BigInteger roteiroId,
									 SalvaArquivoRequest salvaArquivo,
									 MultipartFile arquivo,
									 String authorization) throws Exception {
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo.getCnpjEmpresa(), salvaArquivo.getCnpjContabilidade(), salvaArquivo.getApplicationId(), arquivo, authorization).getBody();
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		roteiro = roteiro.toBuilder().status((short) 3).urlArquivo(arquivoS3.getId().toString()).build();
		
		Empresa empresa = empresaRepository.buscarPorId(roteiro.getEmpresaId()).orElseThrow(() -> new NoResultException("Empresa nao encontrada!"));
		SFEmpresa empresaCrm = SFEmpresa.builder()
				.Arquivo_Portal(S3_SERVICE_URL+"/api/v1/arquivos/"+arquivoS3.getId().toString()+"/download")
			.build();
		sfClient.upsertEmpresa(empresa.getNomeResumido(), empresaCrm, authorization);
		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	public RoteiroDTO patch(BigInteger roteiroId, RoteiroDTO roteiroDTO, OAuth2Authentication authentication) throws Exception {
		UserDTO userInfo = oauthClient.getUserInfo(getAuthorizationHeader(authentication)).getBody().getRecord();
		roteiroDTO.setUsuario(userInfo.getUsername());
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		if(roteiroDTO.getNome() != null && !roteiroDTO.getNome().equals("")) {
			if(repository.buscaPorNomeEmpresaIdTipo(roteiroDTO.getNome(), roteiro.getEmpresaId(), roteiro.getTipoRoteiro()) > 0)
				throw new IllegalArgumentException("Nome de roteiro já existente nesta empresa para este tipo de roteiro!");
			
		}
		return RoteiroMapper.fromEntity(repository.save(roteiroDTO.patch(roteiro)));
	}
	
	public Page<Roteiro> busca(RoteiroDTO filtro, PageCriteria criteria) throws Exception {
		return repository.buscaRoteiros(filtro, criteria);
	}
	
	public String deleta(BigInteger roteiroId) throws Exception {
		repository.deleteById(roteiroId);
		return "Roteiro removido com sucesso!";
	}

	private String getAuthorizationHeader(OAuth2Authentication authentication) {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        String accessToken = details.getTokenValue();
        return MessageFormat.format("Bearer {0}", accessToken);
    }
	
}
