package br.com.ottimizza.integradorcloud.client;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;

@FeignClient(name = "${storage-s3.service.name}", url = "${storage-s3.service.url}")
public interface StorageS3Client {

	@PostMapping("/api/v1/arquivos")
	ResponseEntity<?> uploadArquivo(@Valid SalvaArquivoRequest salvaArquivo,
									@RequestParam MultipartFile arquivo,
									OAuth2Authentication authentication);
	
	
}
