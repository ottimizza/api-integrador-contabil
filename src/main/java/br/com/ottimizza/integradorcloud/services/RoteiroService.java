package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;

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
	
	
	public ResponseEntity<?> uploadPlanilha(BigInteger idRoteiro,
											SalvaArquivoRequest salvaArquivo,
											MultipartFile arquivo,
											OAuth2Authentication authentication) throws Exception {
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo, arquivo, authentication).getBody();
		return ResponseEntity.ok(arquivoS3);
	}


}
