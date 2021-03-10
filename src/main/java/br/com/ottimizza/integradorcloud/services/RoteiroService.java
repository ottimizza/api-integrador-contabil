package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.integradorcloud.client.EmailSenderClient;
import br.com.ottimizza.integradorcloud.client.OAuthClient;
import br.com.ottimizza.integradorcloud.client.SalesForceClient;
import br.com.ottimizza.integradorcloud.client.StorageS3Client;
import br.com.ottimizza.integradorcloud.domain.commands.roteiro.SalvaArquivoRequest;
import br.com.ottimizza.integradorcloud.domain.criterias.PageCriteria;
import br.com.ottimizza.integradorcloud.domain.dtos.ArquivoS3DTO;
import br.com.ottimizza.integradorcloud.domain.dtos.CheckListPerguntasRespostasDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.EmailDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFRoteiro;
import br.com.ottimizza.integradorcloud.domain.mappers.RoteiroMapper;
import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;
import br.com.ottimizza.integradorcloud.repositories.ContabilidadeRepository;
import br.com.ottimizza.integradorcloud.repositories.EmpresaRepository;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListRespostasRepository;
import br.com.ottimizza.integradorcloud.repositories.roteiro.RoteiroRepository;
import br.com.ottimizza.integradorcloud.utils.ServiceUtils;

@Service
public class RoteiroService {

	@Inject
	RoteiroRepository repository;
	
	@Inject
	EmpresaRepository empresaRepository;
	
	@Inject
	ContabilidadeRepository contabilidadeRepository;
	
	@Inject
	CheckListRespostasRepository checklistRepository;
	
	@Inject
	StorageS3Client s3Client;
	
	@Inject
	OAuthClient oauthClient;
	
	@Inject
	SalesForceClient sfClient;
	
	@Inject
	EmailSenderClient emailSenderClient;
	
	@Value("${storage-s3.service.url}")
    private String S3_SERVICE_URL;
	
	@Value("${salesforce.service.url}")
    private String SF_SERVICE_URL;
	
	@Value("${email-envio-checklist}")
	private String EMAIL_ENVIO_CHECKLIST;
	
	public RoteiroDTO salva(RoteiroDTO roteiroDTO, OAuth2Authentication authentication) throws Exception {
		roteiroDTO.setUsuario(authentication.getName());
		
		Roteiro roteiro = RoteiroMapper.fromDTO(roteiroDTO);
		validaRoteiro(roteiro);
		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	public RoteiroDTO uploadPlanilha(BigInteger roteiroId,
									 SalvaArquivoRequest salvaArquivo,
									 MultipartFile arquivo,
									 String authorization,
									 OAuth2Authentication authentication) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder soql = new StringBuilder();
		String tipoRoteiro = "";
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo.getCnpjEmpresa(), salvaArquivo.getCnpjContabilidade(), salvaArquivo.getApplicationId(), arquivo, authorization).getBody();
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		roteiro = roteiro.toBuilder().status((short) 5).urlArquivo(S3_SERVICE_URL+"/resources/"+arquivoS3.getId().toString()+"/download").build();
		validaRoteiro(roteiro);
		Empresa empresa = empresaRepository.buscarPorId(roteiro.getEmpresaId()).orElseThrow(() -> new NoResultException("Empresa nao encontrada!"));
		
		Contabilidade contabilidade = contabilidadeRepository.buscaPorCnpj(roteiro.getCnpjContabilidade());
		SFEmpresa empresaCrm = SFEmpresa.builder()
				.Arquivo_Portal(S3_SERVICE_URL+"/resources/"+arquivoS3.getId().toString()+"/download")
				.Contabilidade_Id(contabilidade.getSalesForceId())
			.build();
		String empresaCrmString = mapper.writeValueAsString(empresaCrm);
		ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+empresa.getNomeResumido(), empresaCrmString, authorization);
		
		//------------------------
		
		//SFEmpresa sfEmpresa = sfClient.getEmpresa(empresa.getNomeResumido(), ServiceUtils.getAuthorizationHeader(authentication)).getBody();
		
		if(roteiro.getTipoRoteiro().equals("PAG"))
			tipoRoteiro = "Contas PAGAS";
		else
			tipoRoteiro = "Contas RECEBIDAS";
		
		String chaveOic = empresa.getCnpj()+"-"+roteiro.getTipoRoteiro();
		
		soql.append("SELECT Name, Id ");
		soql.append("FROM Roteiros__c ");
		soql.append("WHERE Chave_OIC_Integracao = "+chaveOic);
	    
		//SFRoteiro response = (SFRoteiro) sfClient.executeSOQL(soql.toString(), 1, ServiceUtils.getAuthorizationHeader(authentication)).getBody();
		
		//System.out.println(response.toString());
	    //if (response == null) {
	    	SFRoteiro sfRoteiro = SFRoteiro.builder()
	    			//.chaveOic(chaveOic)
	    			.empresaId(empresa.getNomeResumido())
	    			.tipoIntegracao(tipoRoteiro)
	    			.nomeRelatorioReferencia("Principal")
	    			.fornecedor("-1")
	    			.portador("-1")
	    			.dataMovimento("-1")
	    			.lerPlanilhasPadroes(true)
	    		.build();
	    	sfClient.upsertRoteiro(chaveOic, sfRoteiro, ServiceUtils.getAuthorizationHeader(authentication));
	   // }
	    
		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	public RoteiroDTO patch(BigInteger roteiroId, RoteiroDTO roteiroDTO, OAuth2Authentication authentication) throws Exception {
		StringBuilder email = new StringBuilder();
		roteiroDTO.setUsuario(authentication.getName());
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		if(roteiroDTO.getNome() != null && !roteiroDTO.getNome().equals("")) {
			if(repository.buscaPorNomeEmpresaIdTipo(roteiroDTO.getNome(), roteiro.getEmpresaId(), roteiro.getTipoRoteiro()) > 0)
				throw new IllegalArgumentException("Nome de roteiro já existente nesta empresa para este tipo de roteiro!");
			
			List<CheckListPerguntasRespostasDTO> perguntasRespostas = checklistRepository.buscaPerguntasRespostasPorRoteiroId(roteiroId);
			for(CheckListPerguntasRespostasDTO cp : perguntasRespostas) {
				email.append(cp.getPergunta()+": "+cp.getResposta()+", Observacao: "+cp.getObservacao()+" ");
				email.append("<br>");
			}
		}
		if(roteiroDTO.getTipoRoteiro() != null && !roteiroDTO.getTipoRoteiro().equals("")) {
			if(roteiroDTO.getTipoRoteiro().contains("PAG"))
				roteiroDTO.setTipoProjeto((short) 1);
			else
				roteiroDTO.setTipoProjeto((short) 2);
			
		}
		Roteiro retorno = roteiroDTO.patch(roteiro);
		validaRoteiro(retorno);
		Roteiro roteiroRetorno = repository.save(retorno);
		if(roteiroDTO.getNome() != null && !roteiroDTO.getNome().equals("")) {
			UserDTO userInfo = oauthClient.getUserInfo(ServiceUtils.getAuthorizationHeader(authentication)).getBody().getRecord();
			Empresa empresa = empresaRepository.buscaEmpresa(roteiro.getCnpjEmpresa(), userInfo.getOrganization().getId()).orElse(null);

			EmailDTO mail = EmailDTO.builder()
					.to(EMAIL_ENVIO_CHECKLIST)
					.subject("Check List Integração "+roteiro.getTipoRoteiro()+" " + empresa.getRazaoSocial() +" " +userInfo.getOrganization().getName())
					.body(email.toString().replaceAll("null", ""))
				.build();
			emailSenderClient.sendMail(mail);
		}
		
		return RoteiroMapper.fromEntity(roteiroRetorno);
	}
	
	public Page<Roteiro> busca(RoteiroDTO filtro, PageCriteria criteria) throws Exception {
		return repository.buscaRoteiros(filtro, criteria);
	}
	
	public String deleta(BigInteger roteiroId) throws Exception {
		repository.deleteById(roteiroId);
		return "Roteiro removido com sucesso!";
	}

	private boolean validaRoteiro(Roteiro roteiro) throws Exception {
		if(roteiro.getStatus() == 1) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
		}
		if(roteiro.getStatus() == 3) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
		}
		if(roteiro.getStatus() == 5) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
			if(roteiro.getTipoRoteiro() == null || roteiro.getTipoRoteiro().equals(""))
				throw new IllegalArgumentException("Informe o tipo do roteiro!");
			if(roteiro.getTipoProjeto() == null || roteiro.getTipoProjeto().equals(""))
				throw new IllegalArgumentException("Tipo projeto não encontrado para este roteiro!");
		}
		if(roteiro.getStatus() == 6) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
			if(roteiro.getTipoRoteiro() == null || roteiro.getTipoRoteiro().equals(""))
				throw new IllegalArgumentException("Informe o tipo do roteiro!");
			if(roteiro.getTipoProjeto() == null || roteiro.getTipoProjeto().equals(""))
				throw new IllegalArgumentException("Tipo projeto não encontrado para este roteiro!");
			if(roteiro.getChecklist() == null)
				throw new IllegalArgumentException("Checklist não encontrada para este roteiro!"); 
		}
		if(roteiro.getStatus() == 7) {
			if(roteiro.getCnpjContabilidade() == null || roteiro.getCnpjContabilidade().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da contabilidade relacionado ao roteiro!");
			if(roteiro.getContabilidadeId() == null)
				throw new IllegalArgumentException("Informe o id da contabilidade relacioando ao roteiro!");
			if(roteiro.getCnpjEmpresa() == null || roteiro.getCnpjEmpresa().equals(""))
				throw new IllegalArgumentException("Informe o cnpj da empresa relacionado ao roteiro!");
			if(roteiro.getEmpresaId() == null)
				throw new IllegalArgumentException("Informe o id da empresa relacioando ao roteiro!");
			if(roteiro.getUrlArquivo() == null || roteiro.getUrlArquivo().equals(""))
				throw new IllegalArgumentException("Url arquivo relacioando ao roteiro não foi encontrada!");
			if(roteiro.getTipoRoteiro() == null || roteiro.getTipoRoteiro().equals(""))
				throw new IllegalArgumentException("Informe o tipo do roteiro!");
			if(roteiro.getTipoProjeto() == null || roteiro.getTipoProjeto().equals(""))
				throw new IllegalArgumentException("Tipo projeto não encontrado para este roteiro!");
			if(roteiro.getChecklist() == null)
				throw new IllegalArgumentException("Checklist não encontrada para este roteiro!"); 
			if(roteiro.getNome() == null || roteiro.getNome().equals(""))
				throw new IllegalArgumentException("Informe o nome para o seu roteiro!"); 
		}
		return true;
	}
}
