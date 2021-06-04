package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
import br.com.ottimizza.integradorcloud.domain.dtos.LayoutPadraoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.RoteiroDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.UserDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFEmpresa;
import br.com.ottimizza.integradorcloud.domain.dtos.sForce.SFRoteiro;
import br.com.ottimizza.integradorcloud.domain.mappers.RoteiroMapper;
import br.com.ottimizza.integradorcloud.domain.models.Contabilidade;
import br.com.ottimizza.integradorcloud.domain.models.Empresa;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.LayoutPadrao;
import br.com.ottimizza.integradorcloud.domain.models.roteiro.Roteiro;
import br.com.ottimizza.integradorcloud.repositories.ContabilidadeRepository;
import br.com.ottimizza.integradorcloud.repositories.EmpresaRepository;
import br.com.ottimizza.integradorcloud.repositories.RoteiroLayoutRepository;
import br.com.ottimizza.integradorcloud.repositories.checklist.CheckListRespostasRepository;
import br.com.ottimizza.integradorcloud.repositories.layout_padrao.LayoutPadraoRepository;
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
	RoteiroLayoutRepository roteiroLayoutRepository;

	@Inject
	LayoutPadraoRepository layoutRepository;

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
		
		if(roteiroDTO.getUtilizaOMC()){
			ObjectMapper mapper = new ObjectMapper();
			Empresa empresa = empresaRepository.buscarPorCNPJ(roteiroDTO.getCnpjEmpresa()).orElseThrow(() -> new NoResultException("Empresa nao encontrada!"));
			SFEmpresa empresaSf = sfClient.getEmpresa(empresa.getNomeResumido(), ServiceUtils.getAuthorizationHeader(authentication)).getBody();
			empresaSf.setIdEmpresa(null);
			empresaSf.setNome_Resumido(null);
			empresaSf.setPossui_OMC__c("true");

			String empresaCrmString = mapper.writeValueAsString(empresaSf);
			ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+empresa.getNomeResumido(), empresaCrmString, ServiceUtils.getAuthorizationHeader(authentication));

			empresa.setPossuiOmc(true);
			empresaRepository.save(empresa);
		}

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
		String tipoRoteiro = "";
		StringBuilder urlArquivoPortal = new StringBuilder();
		ArquivoS3DTO arquivoS3 = s3Client.uploadArquivo(salvaArquivo.getCnpjEmpresa(), salvaArquivo.getCnpjContabilidade(), salvaArquivo.getApplicationId(), arquivo, authorization).getBody();
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		roteiro = roteiro.toBuilder().status((short) 5).urlArquivo(S3_SERVICE_URL+"/resources/"+arquivoS3.getId().toString()+"/download").build();
		validaRoteiro(roteiro);
		Empresa empresa = empresaRepository.buscarPorId(roteiro.getEmpresaId()).orElseThrow(() -> new NoResultException("Empresa nao encontrada!"));
		
		Contabilidade contabilidade = contabilidadeRepository.buscaPorCnpj(roteiro.getCnpjContabilidade());
		SFEmpresa empresaCrm = SFEmpresa.builder()
				.Contabilidade_Id(contabilidade.getSalesForceId())
			.build();
		String empresaCrmString = mapper.writeValueAsString(empresaCrm);
		ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+empresa.getNomeResumido(), empresaCrmString, authorization);

		//------------------------

		SFEmpresa sfEmpresa = sfClient.getEmpresa(empresa.getNomeResumido(), ServiceUtils.getAuthorizationHeader(authentication)).getBody();

		if(roteiro.getTipoRoteiro().equals("PAG"))
			tipoRoteiro = "Contas PAGAS";
		else
			tipoRoteiro = "Contas RECEBIDAS";

		String chaveOic = empresa.getCnpj()+"-"+roteiro.getTipoRoteiro();

		SFRoteiro roteiroSF;
		try{
			roteiroSF = sfClient.getRoteiro(chaveOic, ServiceUtils.getAuthorizationHeader(authentication)).getBody();
			roteiroSF.setChaveOic(null);
			roteiroSF.setIdRoteiro(null);
		}
		catch(Exception ex){
	    	roteiroSF = SFRoteiro.builder()
	    			.empresaId(sfEmpresa.getIdEmpresa())
	    			.tipoIntegracao(tipoRoteiro)
	    			.nomeRelatorioReferencia("Principal")
	    			.fornecedor("-1")
	    			.portador("-1")
	    			.dataMovimento("-1")
					.valorDocumento("-1")
	    			.lerPlanilhasPadroes(true)
	    		.build();
	    	
		}

		if(roteiroSF.getArquivoDoPortal() != null && !roteiroSF.getArquivoDoPortal().equals("")){
			urlArquivoPortal.append(roteiroSF.getArquivoDoPortal());
			urlArquivoPortal.append("\n");
		}

		urlArquivoPortal.append("Data Upload: "+ LocalDate.now(ZoneId.of("Brazil/East")));
		urlArquivoPortal.append("\n");
		urlArquivoPortal.append(S3_SERVICE_URL+"/resources/v2/"+arquivoS3.getUuid().toString()+"/download");
		roteiroSF.setArquivoDoPortal(urlArquivoPortal.toString());
		sfClient.upsertRoteiro(chaveOic, roteiroSF, ServiceUtils.getAuthorizationHeader(authentication));

		return RoteiroMapper.fromEntity(repository.save(roteiro));
	}
	
	public RoteiroDTO patch(BigInteger roteiroId, RoteiroDTO roteiroDTO, OAuth2Authentication authentication) throws Exception {
		StringBuilder email = new StringBuilder();
		roteiroDTO.setUsuario(authentication.getName());
		Roteiro roteiro = repository.findById(roteiroId).orElseThrow(() -> new NoResultException("Roteiro nao encontrado!"));
		if(roteiroDTO.getNome() != null && !roteiroDTO.getNome().equals("")) {
			if(repository.buscaPorNomeEmpresaIdTipo(roteiroDTO.getNome(), roteiro.getEmpresaId(), roteiro.getTipoRoteiro()) > 0)
				throw new IllegalArgumentException("Nome de roteiro já existente nesta empresa para este tipo de roteiro!");


			try{
			List<CheckListPerguntasRespostasDTO> perguntasRespostas = checklistRepository.buscaPerguntasRespostasPorRoteiroId(roteiroId);
			for(CheckListPerguntasRespostasDTO cp : perguntasRespostas) {
				email.append(cp.getPergunta()+": "+cp.getResposta()+", Observacao: "+cp.getObservacao()+" ");
				email.append("<br>");
			}
			}
			catch(Exception ex){}
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
		List<BigInteger> idsLayouts = null;
		try{
			idsLayouts = roteiroLayoutRepository.getLayoutsIdByRoteiroId(roteiroId);
		}
		catch(Exception ex) { }
		SFRoteiro roteiroSF = null;
		if(idsLayouts != null && idsLayouts.size() > 0){
			String layouts = "";
			String chaveOic = roteiro.getCnpjEmpresa()+"-"+roteiro.getTipoRoteiro();
			try{
				roteiroSF = sfClient.getRoteiro(chaveOic, ServiceUtils.getAuthorizationHeader(authentication)).getBody();
				roteiroSF.setChaveOic(null);
				roteiroSF.setIdRoteiro(null);
			}
			catch(Exception ex){ }
			for(BigInteger layoutId : idsLayouts) {
				LayoutPadrao layout = layoutRepository.findById(layoutId).orElse(null);
				layouts = layouts + layout.getIdSalesForce()+";";
			}
			layouts = layouts.substring(0, layouts.length() - 1);
			roteiroSF.setPlanilhasPadroes(layouts);
			sfClient.upsertRoteiro(chaveOic, roteiroSF, ServiceUtils.getAuthorizationHeader(authentication));
		}
		if(roteiroDTO.getNome() != null && !roteiroDTO.getNome().equals("") && !roteiroDTO.getNome().contains("TESTE")) {
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

	public List<RoteiroDTO> salvarRoteiroLayouts(RoteiroDTO roteiroDTO, List<LayoutPadraoDTO> layouts, OAuth2Authentication authentication) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<String> tiposRoteiro = new ArrayList<>();
		Empresa empresa = empresaRepository.buscarPorId(roteiroDTO.getEmpresaId()).orElseThrow(() -> new NoResultException("Empresa nao encontrada!"));

		Contabilidade contabilidade = contabilidadeRepository.buscaPorCnpj(roteiroDTO.getCnpjContabilidade());
		SFEmpresa empresaCrm = SFEmpresa.builder()
				.Contabilidade_Id(contabilidade.getSalesForceId())
			.build();

		if(roteiroDTO.getUtilizaOMC()){
			empresaCrm.setPossui_OMC__c("true");
			
			empresa.setPossuiOmc(true);
			empresaRepository.save(empresa);
		}			
		String empresaCrmString = mapper.writeValueAsString(empresaCrm);
		ServiceUtils.defaultPatch(SF_SERVICE_URL+"/api/v1/salesforce/sobjects/Empresa__c/Nome_Resumido__c/"+empresa.getNomeResumido(), empresaCrmString, ServiceUtils.getAuthorizationHeader(authentication));
		
		SFEmpresa sfEmpresa = sfClient.getEmpresa(empresa.getNomeResumido(), ServiceUtils.getAuthorizationHeader(authentication)).getBody();

		//-------------------------------------------- VALIDANDO LAYOUTS 

		String layoutsRoteiroPAG = "";
		String layoutsRoteiroREC = "";
		String roteiroAdicionalPAG = "";
		String roteiroAdicionalREC = "";
		for(LayoutPadraoDTO layout : layouts) {
			if(layout.getIdSalesForce().startsWith("ROT") && layout.getTipoIntegracao() == LayoutPadrao.TipoIntegracao.ERPS) {
				if(layout.getPagamentos() && layout.getRecebimentos()){
					roteiroAdicionalPAG = sfClient.getRoteiroByName(layout.getIdSalesForce(), ServiceUtils.getAuthorizationHeader(authentication)).getBody().getIdRoteiro();
					roteiroAdicionalREC = roteiroAdicionalPAG;
				}
				else if(layout.getPagamentos())
					roteiroAdicionalPAG = sfClient.getRoteiroByName(layout.getIdSalesForce(), ServiceUtils.getAuthorizationHeader(authentication)).getBody().getIdRoteiro();
				else
					roteiroAdicionalREC = sfClient.getRoteiroByName(layout.getIdSalesForce(), ServiceUtils.getAuthorizationHeader(authentication)).getBody().getIdRoteiro();
			}
			else if(!layout.getIdSalesForce().startsWith("ROT")){
				if(layout.getPagamentos()) {
					layoutsRoteiroPAG = layoutsRoteiroPAG + layout.getIdSalesForce()+";";
				}
				if(layout.getRecebimentos()) {
					layoutsRoteiroREC = layoutsRoteiroREC + layout.getIdSalesForce()+";";
				}
			}
		}

		if(layoutsRoteiroREC != null && !layoutsRoteiroREC.equals("")){
			int rec = layoutsRoteiroREC.lastIndexOf(";");
			if(rec == layoutsRoteiroREC.length() - 1) {
				layoutsRoteiroREC = layoutsRoteiroREC.substring(0, layoutsRoteiroREC.length() - 1);
			} 
		}
		if(layoutsRoteiroPAG != null && !layoutsRoteiroPAG.equals("")){
			int pag = layoutsRoteiroPAG.lastIndexOf(";");
			if(pag == layoutsRoteiroPAG.length() - 1) {
				layoutsRoteiroPAG = layoutsRoteiroPAG.substring(0, layoutsRoteiroPAG.length() - 1);
			}
		}

		if(roteiroDTO.getTipoRoteiro().contains("PAG"))
			tiposRoteiro.add("Contas PAGAS");

		if(roteiroDTO.getTipoRoteiro().contains("REC"))
			tiposRoteiro.add("Contas RECEBIDAS");

		//------------------------------------
		List<RoteiroDTO> retorno = new ArrayList<>();
		Roteiro roteiro = null;
		for(String tipoRot : tiposRoteiro) {			
			String tipoRoteiro = tipoRot.substring(tipoRot.indexOf(" ")).substring(1, 4);
			roteiroDTO.setTipoRoteiro(tipoRoteiro);
			roteiro = repository.save(RoteiroMapper.fromDTO(roteiroDTO));
			String chaveOic = empresa.getCnpj()+"-"+tipoRoteiro;

			SFRoteiro roteiroSF;
			try{
				roteiroSF = sfClient.getRoteiro(chaveOic, ServiceUtils.getAuthorizationHeader(authentication)).getBody();
				roteiroSF.setChaveOic(null);
				roteiroSF.setIdRoteiro(null);
			}
			catch(Exception ex){
	    		roteiroSF = SFRoteiro.builder()
	    			.empresaId(sfEmpresa.getIdEmpresa())
	    			.tipoIntegracao(tipoRot)
	    			.nomeRelatorioReferencia("Principal")
	    			.fornecedor("-1")
	    			.portador("-1")
	    			.dataMovimento("-1")
					.valorDocumento("-1")
	    			.lerPlanilhasPadroes(true)
	    		.build();
	    	
			}
			if(tipoRoteiro.contains("REC")){
				roteiroSF.setPlanilhasPadroes(layoutsRoteiroREC);
				if(!roteiroAdicionalREC.equals(""))
					roteiroSF.setRoteiroCompartilhadoAdicional(roteiroAdicionalREC);
			}
			else {
				roteiroSF.setPlanilhasPadroes(layoutsRoteiroPAG);
				if(!roteiroAdicionalPAG.equals(""))
					roteiroSF.setRoteiroCompartilhadoAdicional(roteiroAdicionalPAG);
			}
			sfClient.upsertRoteiro(chaveOic, roteiroSF, ServiceUtils.getAuthorizationHeader(authentication));
			retorno.add(RoteiroMapper.fromEntity(roteiro));
		}
		return retorno;
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
