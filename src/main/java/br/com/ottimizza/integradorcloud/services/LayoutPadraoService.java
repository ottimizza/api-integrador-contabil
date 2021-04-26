package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LayoutPadraoDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.LayoutsPadroesMapper;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;
import br.com.ottimizza.integradorcloud.repositories.layout_padrao.LayoutPadraoRepository;

@Service
public class LayoutPadraoService {
    
    @Inject
    LayoutPadraoRepository repository;

    public Page<LayoutPadrao> buscaComFiltro(LayoutPadraoDTO filtro, PageCriteria criteria) throws Exception {
        return repository.buscaComFiltro(filtro, criteria);
    }

    public LayoutPadraoDTO save(LayoutPadraoDTO layoutDto) throws Exception {
        LayoutPadrao layout = repository.save(LayoutsPadroesMapper.fromDTO(layoutDto));
        return LayoutsPadroesMapper.fromEntity(layout);
    }

    public LayoutPadraoDTO patch(BigInteger id, LayoutPadraoDTO layoutDto) throws Exception {
        LayoutPadrao layout = repository.findById(id).orElseThrow(() -> new NoResultException("Layout padrao nao encontrado!"));
        return LayoutsPadroesMapper.fromEntity(repository.save(layoutDto.patch(layout)));
    }

    public String deletaPorId(BigInteger id) throws Exception {
        repository.deleteById(id);
        return "Layout Padrão removido com sucesso";
    }
}
