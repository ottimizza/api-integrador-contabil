package br.com.ottimizza.integradorcloud.services;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.BancoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.BancoMapper;
import br.com.ottimizza.integradorcloud.domain.models.Banco;
import br.com.ottimizza.integradorcloud.repositories.BancoRepository;

public class BancoService {

	@Inject
	OAuthClient oauthClient;
	
	@Inject
	BancoRepository bancoRepository;
	
	public BancoDTO salvar(BancoDTO bancoDto, OAuth2Authentication authentication) {
		return BancoMapper.fromEntity(bancoRepository.save(BancoMapper.fromDto(bancoDto)));
	}

	public Page<BancoDTO> buscarBancos(@Valid BancoDTO filter, 
									   @Valid PageCriteria pageCriteria,
									   OAuth2Authentication authentication) {
		
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);
		Example<Banco> example = Example.of(BancoMapper.fromDto(filter), matcher);
		return bancoRepository.findAll(example, PageRequest.of(pageCriteria.getPageIndex(), pageCriteria.getPageSize())).map(BancoMapper::fromEntity);
		
	}
	
    
}
