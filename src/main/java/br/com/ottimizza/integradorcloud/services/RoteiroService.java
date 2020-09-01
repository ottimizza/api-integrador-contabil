package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.ArquivoS3DTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.client.StorageS3Client;
import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.roteiro.RoteiroMapper;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;
import br.com.ottimizza.integradorcloud.repositories.roteiro.RoteiroRepository;

@Service
public class RoteiroService {

	@Inject
	RoteiroRepository repository;
	
	@Inject
	StorageS3Client s3Client;
	
	public RoteiroDTO salva(RoteiroDTO roteiroDTO) throws Exception {
		Roteiro roteiro = RoteiroMapper.fromDTO(roteiroDTO);
		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	
	public ResponseEntity<?> uploadPlanilha(BigInteger roteiroId,
											SalvaArquivoRequest salvaArquivo,
											MultipartFile arquivo,
											String authorization) throws Exception {
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo, arquivo, authorization).getBody();
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		roteiro.setUrlArquivo(arquivoS3.getId().toString());
		repository.save(roteiro);
		return ResponseEntity.ok(arquivoS3);
	}
	
	public RoteiroDTO patch(BigInteger roteiroId, RoteiroDTO roteiroDTO) throws Exception {
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		return RoteiroMapper.fromEntity(repository.save(roteiroDTO.patch(roteiro)));
	}


}
