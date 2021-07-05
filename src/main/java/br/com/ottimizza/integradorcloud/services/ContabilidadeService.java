package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.dtos.ContabilidadeDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.ContabilidadeMapper;
import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import br.com.ottimizza.integradorcloud.repositories.ContabilidadeRepository;

@Service
public class ContabilidadeService {
    
    @Inject
    ContabilidadeRepository contabilidadeRepository;

    public ContabilidadeDTO salvaContabilidade(ContabilidadeDTO contabilidadeDTO) throws Exception {
        Contabilidade contabilidade = ContabilidadeMapper.fromDTO(contabilidadeDTO);
        return ContabilidadeMapper.fromEntity(contabilidadeRepository.save(contabilidade));
    }

    public ContabilidadeDTO patchContabilidade(BigInteger contabilidadeId, ContabilidadeDTO contabildiadeDto) throws Exception {
        Contabilidade contabilidade = contabilidadeRepository.findById(contabilidadeId).orElseThrow(() -> new NoResultException("Contabilidade não encontrada!"));
        return ContabilidadeMapper.fromEntity(contabilidadeRepository.save(contabildiadeDto.patch(contabilidade)));
    }

    public ContabilidadeDTO buscaPorId(BigInteger contabilidadeId) throws Exception {
        return ContabilidadeMapper.fromEntity(contabilidadeRepository.findById(contabilidadeId).orElseThrow(() -> new NoResultException("Contabilidade não encontrada!")));
    }

    public ContabilidadeDTO buscaPorOauthId(BigInteger oauthId) throws Exception {
        return ContabilidadeMapper.fromEntity(contabilidadeRepository.findByOuathId(oauthId));
    }

}
