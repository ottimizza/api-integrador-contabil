package br.com.ottimizza.integradorcloud.services;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.integradorcloud.client.KafkaClient;
import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.client.StorageS3Client;
import br.com.ottimizza.integradorcloud.domain.commands.livro_caixa.ImprortacaoLivroCaixas;
import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.ArquivoS3DTO;
import br.com.ottimizza.integradorcloud.domain.dtos.GrupoRegraDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaImportadoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
import br.com.ottimizza.integradorcloud.domain.mappers.GrupoRegraMapper;
import br.com.ottimizza.integradorcloud.domain.mappers.LivroCaixaMapper;
import br.com.ottimizza.integradorcloud.domain.models.GrupoRegra;
import br.com.ottimizza.integradorcloud.domain.models.LivroCaixa;
import br.com.ottimizza.integradorcloud.domain.models.banco.Banco;
import br.com.ottimizza.integradorcloud.domain.models.banco.BancosPadroes;
import br.com.ottimizza.integradorcloud.domain.models.banco.SaldoBancos;
import br.com.ottimizza.integradorcloud.repositories.BancosPadroesRepository;
import br.com.ottimizza.integradorcloud.repositories.banco.BancoRepository;
import br.com.ottimizza.integradorcloud.repositories.livro_caixa.LivroCaixaRepository;
import br.com.ottimizza.integradorcloud.repositories.saldo_bancos.SaldoBancosRepository;
import br.com.ottimizza.integradorcloud.utils.ServiceUtils;

@Service
public class LivroCaixaService {
	
	@Value("${storage-s3.service.url}")
    private String S3_SERVICE_URL;

	@Inject
	LivroCaixaRepository repository;

	@Inject 
	BancoRepository bancoRepository;
	
	@Inject
	BancosPadroesRepository bancosPadroesRepository;

	@Inject 
	SaldoBancosRepository saldoRepository;

	@Inject
	OAuthClient oAuthClient; 
	
	@Inject
	StorageS3Client s3Client;

	@Inject
	KafkaClient kafkaClient;
	
	public LivroCaixaDTO salva(LivroCaixaDTO livroCaixa, OAuth2Authentication authentication) throws Exception {
		validaLivroCaixa(livroCaixa);
		UserDTO user = oAuthClient.getUserInfo(ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
		SaldoBancos ultimoSaldo = saldoRepository.buscaPorBancoDataMaior(livroCaixa.getBancoId(), livroCaixa.getDataMovimento());
		if(ultimoSaldo != null) {
			throw new IllegalArgumentException("O mês informado já foi encerrado e dados enviados a contabilidade.");
		}
		if(user.getUsername() != null && !user.getUsername().equals(""))
			livroCaixa.setCriadoPor(user.getUsername());
		LivroCaixa retorno = repository.save(LivroCaixaMapper.fromDTO(livroCaixa));
		return LivroCaixaMapper.fromEntity(retorno);
	}
	
	public LivroCaixaDTO patch(BigInteger id, LivroCaixaDTO livroCaixaDTO) throws Exception {
		LivroCaixa livroCaixa = repository.findById(id).orElseThrow(() -> new NoResultException("Livro Caixa nao encontrado!"));
		if(livroCaixa.getOrigem() == 1){
			throw new IllegalArgumentException("Não é possível alterar este livro caixa");
		}
		LivroCaixa retorno = repository.save(livroCaixaDTO.patch(livroCaixa));
		return LivroCaixaMapper.fromEntity(retorno);
	}
	
	public Page<LivroCaixa> buscaComFiltro(LivroCaixaDTO filtro, PageCriteria criteria) throws Exception {
		return repository.buscaComFiltro(filtro, criteria);
	}
	
	public String deletaPorId(BigInteger id) throws Exception {
		repository.deleteById(id);
		return "Livro Caixa removido com sucesso!";
	}

	public LivroCaixaDTO buscaPorId(BigInteger livroCaixaId, OAuth2Authentication authentication) throws Exception {
		UserDTO user = oAuthClient.getUserInfo(ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
		LivroCaixa livroCaixa = repository.findById(livroCaixaId).orElseThrow(() -> new NoResultException("Livro Caixa nao encontrado!"));
		if(!livroCaixa.getCriadoPor().equals(user.getUsername()))
			return null;
			
		return LivroCaixaMapper.fromEntity(livroCaixa);
	}

	public GrupoRegraDTO sugerirRegra(BigInteger livroCaixaId, String cnpjContabilidade, String cnpjEmpresa) throws Exception {
		try {
			GrupoRegra regraSugerida = repository.sugerirRegra(livroCaixaId, cnpjContabilidade, cnpjEmpresa);
			return GrupoRegraMapper.fromEntity(regraSugerida);
		}
		catch(Exception ex) {
			return null;
		}
	}
	
	public JSONObject deletaNaoIntegrado(BigInteger id) {
		JSONObject response = new JSONObject();
		LivroCaixa livroCaixa = repository.findById(id).orElse(null);
		if (livroCaixa == null) {
			response.put("status", "Error");
			response.put("message", "Houve um problema ao excluir!");
			return response;
		}
		if (!livroCaixa.getIntegradoContabilidade()) {
			try {
				repository.deleteById(id);
				response.put("status", "Success");
				response.put("message", "Excluído com sucesso!");
			} catch (Exception e) {
				response.put("status", "Error");
				response.put("message", "Houve um problema ao excluir!");
				return response;
			}
		} else {
			response.put("status", "Unauthorized");
			response.put("message", "Lançamento ja integrado não pode ser excluido!");
			return response;
		}
		return response;
	}

	public LivroCaixaDTO clonarLivroCaixa(BigInteger id, OAuth2Authentication authentication) {
		LivroCaixa livroCaixa = repository.findById(id).orElse(null);
		if (livroCaixa == null) return null;

		UserDTO userInfo = oAuthClient.getUserInfo(ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
		LivroCaixa novo	 = new LivroCaixa(livroCaixa);
		novo.setCriadoPor(userInfo.getFirstName());
		
		return LivroCaixaMapper.fromEntity(repository.save(novo));
	
	}

	public LivroCaixaDTO buscaUltimoLancamentoContabilidadeEmpresa(String cnpjContabilidade, String cnpjEmpresa) {
		LivroCaixa livroCaixa = repository.findByCnpjContabilidadeAndCnpjEmpresaFirstByOrderByIdDesc(cnpjContabilidade, cnpjEmpresa);
		if(livroCaixa == null) return null;
		return LivroCaixaMapper.fromEntity(livroCaixa);
	}

	public LivroCaixaDTO uploadFile(BigInteger idLivroCaixa, 
									SalvaArquivoRequest salvaArquivo, 
									MultipartFile arquivo, 
									String authorization) throws IOException {
		
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo.getCnpjEmpresa(), salvaArquivo.getCnpjContabilidade(), salvaArquivo.getApplicationId(), arquivo, authorization).getBody();

		LivroCaixa lc = repository.findById(idLivroCaixa).orElseThrow(() -> new NoResultException("livro caixa nao encontrado!"));
		lc.setLinkArquivo(S3_SERVICE_URL+"/resources/v2/"+arquivoS3.getUuid().toString()+"/download");
		
		return LivroCaixaMapper.fromEntity(repository.save(lc));
	}

//	public LivroCaixaDTO integraContabilidade(BigInteger id, String authorization) {
//		LivroCaixa lc = repository.findById(id).orElse(null);
//		lc.setIntegradoContabilidade(true);
//		return LivroCaixaMapper.fromEntity(repository.save(lc));
//	}
	
	public LivroCaixaDTO integraContabilidade(BigInteger id) {
		LivroCaixa lc = repository.findById(id).orElse(null);
		lc.setIntegradoContabilidade(true);
		return LivroCaixaMapper.fromEntity(repository.save(lc));
	}

	public List<LivroCaixaDTO> sugerirLancamento(String cnpjContabilidade, String cnpjEmpresa, String data, Double valor) throws Exception {
		return repository.sugerirLancamento(cnpjContabilidade, cnpjEmpresa, valor, data);
	}
	
	public String integraLivrosCaixas(String cnpjEmpresa, String dataMovimento, BigInteger bancoId) throws Exception {
		StringBuilder obj = new StringBuilder();
		int contador = 1;
		LocalDate data = LocalDate.of(Integer.parseInt(dataMovimento.substring(0, 4)), Integer.parseInt(dataMovimento.substring(5, 7)), Integer.parseInt(dataMovimento.substring(8)));
		List<LivroCaixaDTO> livrosCaixas = LivroCaixaMapper.fromEntities(repository.enviaLivroCaixaNaoIntegrado(cnpjEmpresa, data, bancoId));
		int qntLivros = livrosCaixas.size();
		obj.append("[");
		BigInteger bancoLivroCaixa = BigInteger.ZERO;
		Banco banco = new Banco();
		for(LivroCaixaDTO lc : livrosCaixas){
			if(lc.getBancoId() != bancoLivroCaixa){
				banco = bancoRepository.findById(lc.getBancoId()).orElseThrow(() -> new NoResultException("Banco nao encontrado!"));
			}
			lc.setDescricaoBanco(banco.getDescricao());
			if(contador == qntLivros){
				obj.append(lc.toString());
			}
			else{
				obj.append(lc.toString()+",");
			}
			contador ++;
			bancoLivroCaixa = lc.getBancoId();
		}
		obj.append("]");
		kafkaClient.integradaLivrosCaixas(obj.toString());
		return "livrosCaixas integrados com sucesso!";
	}

	public List<LivroCaixa> importarLivrosCaixas(ImprortacaoLivroCaixas importLivrosCaixas) throws Exception {
		List<LivroCaixa> livrosCaixas = new ArrayList<>();
		Banco banco = new Banco();

		try {
			banco =  bancoRepository.findByCodigoAndCnpjEmpresa(importLivrosCaixas.getBanco(), importLivrosCaixas.getCnpjEmpresa());
		} catch (Exception e) {
			BancosPadroes bancoPadrao = new BancosPadroes();
			try {
				bancoPadrao = bancosPadroesRepository.findByCodigo(importLivrosCaixas.getBanco());
			} catch (Exception e2) { 
				System.out.println("*** Falha ao buscar banco padrao por codigo!");
			}
			banco = bancoRepository.save(Banco.builder()
					.cnpjEmpresa(importLivrosCaixas.getCnpjEmpresa())
					.cnpjContabilidade(importLivrosCaixas.getCnpjContabilidade())
					.nomeBanco(bancoPadrao.getNomeBanco())
					.codigoBanco(bancoPadrao.getCodigoBanco())
					.bancoPadraoId(bancoPadrao.getId())
					.build());
		}
		for(LivroCaixaImportadoDTO lc : importLivrosCaixas.getLivrosCaixas()){
			if(repository.findByIdExterno(lc.getIdExterno()) == null) {
				LivroCaixa livro = LivroCaixa.builder()
						.bancoId(banco.getId())
						.cnpjEmpresa(banco.getCnpjEmpresa())
						.cnpjContabilidade(banco.getCnpjContabilidade())
						.descricao(lc.getDescricao())
						.tipoMovimento(lc.getTipoMovimento())
						.valorOriginal(lc.getValor())
						.valorFinal(lc.getValor())
						.valorPago(lc.getValor())
						.dataMovimento(lc.getData())
						.idExterno(lc.getIdExterno())
						.status(LivroCaixa.Status.PAGO)
						.origem(1)
					.build();
				livrosCaixas.add(livro);
			}
		}
		List<LivroCaixa> retorno = repository.saveAll(livrosCaixas);
		return retorno;
	}

	public Boolean validaLivroCaixa(LivroCaixaDTO livroCaixa) throws Exception {

		if(livroCaixa.getCnpjContabilidade() != null && !livroCaixa.getCnpjContabilidade().equals(""))
			throw new IllegalArgumentException("Informe o cnpj da contabilidade!");

		if(livroCaixa.getCnpjEmpresa() != null && !livroCaixa.getCnpjEmpresa().equals(""))
			throw new IllegalArgumentException("Informe o cnpj da empresa!");
		
		if(livroCaixa.getDataMovimento() != null)
			throw new IllegalArgumentException("Informe a data movimento!");

		if(livroCaixa.getBancoId() != null)
			throw new IllegalArgumentException("Informe o banco do lancamento!");
		
		if(livroCaixa.getDescricao() != null && !livroCaixa.getDescricao().equals(""))
			throw new IllegalArgumentException("Informe a descricao do lancamento!");

		if(livroCaixa.getValorOriginal() != null)
			throw new IllegalArgumentException("Informe o valor do lancamento!");

		return true;
	}

	public void deletaTestesOTT() throws Exception {
		repository.deleteTestes();
	}

}
