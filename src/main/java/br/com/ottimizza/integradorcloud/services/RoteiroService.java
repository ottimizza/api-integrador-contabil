package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.security.Principal;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.ArquivoS3DTO;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.client.StorageS3Client;
import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
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
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo.getCnpjEmpresa(), salvaArquivo.getCnpjContabilidade(), salvaArquivo.getApplicationId(), arquivo, authorization).getBody();
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		roteiro = roteiro.toBuilder().status((short) 3).urlArquivo(arquivoS3.getId().toString()).build();
		repository.save(roteiro);
		return ResponseEntity.ok(arquivoS3);
	}
	
	public RoteiroDTO patch(BigInteger roteiroId, RoteiroDTO roteiroDTO) throws Exception {
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		return RoteiroMapper.fromEntity(repository.save(roteiroDTO.patch(roteiro)));
	}

	public Page<RoteiroDTO> buscaTodos(RoteiroDTO filtro, PageCriteria criteria, Principal principal) throws Exception {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		Example<Roteiro> exemplo = Example.of(RoteiroMapper.fromDTO(filtro), matcher);
		return repository.findAll(exemplo, RoteiroDTO.getPageRequest(criteria)).map(RoteiroMapper::fromEntity);
	}
	
	public String deleta(BigInteger roteiroId) throws Exception {
		repository.deleteById(roteiroId);
		return "Roteiro removido com sucesso!";
	}


}
