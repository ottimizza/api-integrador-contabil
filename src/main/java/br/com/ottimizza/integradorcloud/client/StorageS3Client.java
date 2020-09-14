package br.com.ottimizza.integradorcloud.client;


import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.ArquivoS3DTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;

@FeignClient(name = "${storage-s3.service.name}", url = "${storage-s3.service.url}")
public interface StorageS3Client {

	@PostMapping("/api/v1/arquivos")
	public ResponseEntity<ArquivoS3DTO> uploadArquivo(@RequestParam SalvaArquivoRequest salvaArquivo,
											   		  @RequestParam("file") MultipartFile arquivo,
											   		  @RequestHeader("Authorization") String authorization);
	
	
}
