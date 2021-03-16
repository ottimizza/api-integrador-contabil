package br.com.ottimizza.integradorcloud.repositories.livro_caixa;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.LivroCaixaMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;

public class LivroCaixaRepositoryImpl implements LivroCaixaRepositoryCustom{

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria) {
		String mesAno = "";
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * 		 	");
		sql.append("FROM livro_caixa lc ");
		sql.append("WHERE lc.cnpj_contabilidade = :cnpjContabilidade ");
		sql.append("AND lc.cnpj_empresa = :cnpjEmpresa ");
		
		if(filtro.getId() != null)
			sql.append("AND lc.id = :id ");
		if(filtro.getComplemento() != null && !filtro.getComplemento().equals(""))
			sql.append("AND lc.complemento ILIKE(:complemento) ");
		if(filtro.getDescricao() != null && !filtro.getDescricao().equals(""))
			sql.append("AND lc.descricao ILIKE(:descricao) ");
		if(filtro.getBancoId() != null)
			sql.append("AND lc.fk_banco_id = :bancoId ");
		if(filtro.getCategoriaId() != null) 
			sql.append("AND lc.fk_categoria_id = :categoriaId ");
		if(filtro.getDataString() != null && !filtro.getDataString().equals("")) {
			mesAno = filtro.getDataString();
			sql.append("AND lc.data_movimento > :dataMovimento1 ");
			sql.append("AND lc.data_movimento < :dataMovimento2 ");
		}
		if(filtro.getStatus() != null)
			sql.append("AND lc.status = :status ");
		if(filtro.getTipoMovimento() != null && !filtro.getTipoMovimento().equals(""))
			sql.append("AND lc.tipo_movimento = :tipoMovimento ");
		
		sql.append("ORDER BY lc.data_movimento ASC ");
		
		Query query = em.createNativeQuery(sql.toString(), LivroCaixa.class);
		query.setParameter("cnpjContabilidade", filtro.getCnpjContabilidade());
		query.setParameter("cnpjEmpresa", filtro.getCnpjEmpresa());
		
		if(filtro.getId() != null)
			query.setParameter("id", filtro.getId());
		if(filtro.getComplemento() != null && !filtro.getComplemento().equals(""))
			query.setParameter("complemento", "%"+filtro.getComplemento()+"%");
		if(filtro.getDescricao() != null && !filtro.getDescricao().equals(""))
			query.setParameter("descricao", "%"+filtro.getDescricao()+"%");
		if(filtro.getBancoId() != null)
			query.setParameter("bancoId", filtro.getBancoId());
		if(filtro.getCategoriaId() != null) 
			query.setParameter("categoriaId", filtro.getCategoriaId());
		if(filtro.getDataString() != null && !filtro.getDataString().equals("")) {
			LocalDate data1 = LocalDate.of(Integer.parseInt(mesAno.substring(3)), Integer.parseInt(mesAno.substring(0,1)), 1);
			LocalDate data2 = LocalDate.of(Integer.parseInt(mesAno.substring(3)), Integer.parseInt(mesAno.substring(0,1)), 31);
			query.setParameter("dataMovimento1", data1);
			query.setParameter("dataMovimento2", data2);
		}
		if(filtro.getStatus() != null)
			query.setParameter("status", filtro.getStatus());
		if(filtro.getTipoMovimento() != null && !filtro.getTipoMovimento().equals(""))
			query.setParameter("tipoMovimento", filtro.getTipoMovimento());
		
		long totalElements = query.getResultList().size();
		query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
		query.setMaxResults(criteria.getPageSize());
			
		return new PageImpl<LivroCaixa>(query.getResultList(), PageCriteria.getPageRequest(criteria), totalElements);
	}
	
	@Override
	public GrupoRegra sugerirRegra(BigInteger livroCaixaId, String cnpjContabilidade, String cnpjEmpresa) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT grupo_regras.* ");
		sql.append("FROM grupo_regras  	");
		sql.append("INNER JOIN livros_caixas ON livros_caixas.termos @>  grupo_regras.campos ");
		sql.append("WHERE livros_caixas.id = :livroCaixaId ");
		sql.append("AND grupo_regras.contagem_regras >= 2 ");
		sql.append("AND grupo_regras.ativo = true ");
		sql.append("AND NOT EXISTS(SELECT 1 FROM regras r2 WHERE r2.fk_grupo_regras_id = grupo_regras.id AND r2.condicao = 2) ");
		sql.append("AND grupo_regras.campos not in (SELECT gr.campos FROM grupo_regras_ignoradas gr WHERE gr.cnpj_contabilidade = :cnpjContabilidade) ");
		sql.append("AND grupo_regras.cnpj_contabilidade = :cnpjContabilidade ");
		sql.append("AND grupo_regras.cnpj_empresa = :cnpjEmpresa ");
		sql.append("ORDER BY grupo_regras.contagem_regras DESC, ");
		sql.append("         grupo_regras.peso_regras DESC, ");
		sql.append("		 grupo_regras.id DESC ");
		sql.append("LIMIT 1");
		
		Query query = em.createNativeQuery(sql.toString());
		
		query.setParameter("livroCaixaId", livroCaixaId);
		query.setParameter("cnpjContabilidade", cnpjContabilidade);
		query.setParameter("cnpjEmpresa", cnpjEmpresa);
		
		return (GrupoRegra) query.getSingleResult();
	}

	@Override
	public List<LivroCaixaDTO> sugerirLancamento(String cnpjContabilidade, String cnpjEmpresa, Double valor, String data) {
		StringBuilder sql = new StringBuilder();
		Double valor1 = valor * 0.9;
		Double valor2 = valor * 1.1;
		LocalDate data1 = LocalDate.of(Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5,7)), Integer.parseInt(data.substring(8))).minusDays(33);
		LocalDate data2 = LocalDate.of(Integer.parseInt(data.substring(0, 4)), Integer.parseInt(data.substring(5,7)), Integer.parseInt(data.substring(8))).minusDays(27);

		System.out.println("Valor1: "+valor1 + " Valor2: "+valor2);
		System.out.println("Data1: "+data1.toString()+" Data2: "+data2.toString());

		sql.append("SELECT * ");
		sql.append("FROM livros_caixas lc ");
		sql.append("WHERE lc.cnpj_contabilidade = :cnpjContabilidade ");
		sql.append("AND lc.cnpj_empresa = :cnpjEmpresa ");
		sql.append("AND lc.valor_original >= :valor1 ");
		sql.append("AND lc.valor_original <= :valor2 ");
		sql.append("AND lc.data_movimento >= :data1 ");
		sql.append("AND lc.data_movimento <= :data2 ");
		sql.append("LIMIT 5");

		Query query = em.createNativeQuery(sql.toString(), LivroCaixa.class);

		query.setParameter("cnpjContabilidade", cnpjContabilidade);
		query.setParameter("cnpjEmpresa", cnpjEmpresa);
		query.setParameter("valor1", valor1);
		query.setParameter("valor2", valor2);
		query.setParameter("data1", data1);
		query.setParameter("data2", data2);

		return LivroCaixaMapper.fromEntities(query.getResultList());
	}


}
