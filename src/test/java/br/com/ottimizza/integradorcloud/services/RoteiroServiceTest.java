package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.security.Principal;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.integradorcloud.IntegradorCloudApplication;
import br.com.ottimizza.integradorcloud.domain.dtos.roteiro.RoteiroDTO;

@RunWith(SpringRunner.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = IntegradorCloudApplication.class)
class RoteiroServiceTest {

	public static final String ADMINISTRATOR = "administrator@ottimizza.com.br";
    public static final String ACCOUNTANT = "accountant@ottimizza.com.br";
    public static final String CUSTOMER = "customer@ottimizza.com.br";
    
    @Mock
    private Principal principal;

    @Mock
    private OAuth2Authentication oauth2Authentication;
    
    @Autowired
    RoteiroService roteiroService;
    
    RoteiroDTO roteiro = RoteiroDTO.builder()
    		.cnpjContabilidade("20000000000000")
    		.contabilidadeId(BigInteger.valueOf(1830))
    		.cnpjEmpresa("12123456712312")
    		.empresaId(BigInteger.valueOf(120))
    		.status((short) 1)
    		.urlArquivo("porenquanto")
    	.build();
    
    @Test
    @Order(1)
    public void dadoRoteiroDTO_quandoIniciaProjeto_entaoOK() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	RoteiroDTO created = roteiroService.salva(roteiro, oauth2Authentication);
    	Assertions.assertNotNull(created);
    	Assertions.assertNotNull(created.getId());
    	Assertions.assertNotNull(created.getCnpjContabilidade());
    	Assertions.assertNotNull(created.getContabilidadeId());
    	Assertions.assertNotNull(created.getCnpjEmpresa());
    	Assertions.assertNotNull(created.getEmpresaId());
    	Assertions.assertNotNull(created.getStatus());
    	Assertions.assertNotNull(created.getUsuario());
    	Assertions.assertNotNull(created.getDataCriacao());
    	Assertions.assertNotNull(created.getDataAtualizacao());
    }
    
  /*@Test
    public void dadoRoteiroDTO_quandoFazUploadPlanilha_entaoOK() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	SalvaArquivoRequest info = SalvaArquivoRequest.builder()
    			.cnpjContabilidade("20000000000000")
    			.cnpjEmpresa("12123456712312")
    			.applicationId("armazem-arquivos")
    		.build();
    	
    	RoteiroDTO created = roteiroService.uploadPlanilha(BigInteger.ONE, info, M, oauth2Authentication);
    }*/
    
    @Test
    @Order(2)
    public void dadoRoteiroDTO_quandoSalvaTipoRoteiro_entaoOk() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = RoteiroDTO.builder()
    			.tipoRoteiro("PAG")
    			.status((short) 5)
    		.build();
    	
    	RoteiroDTO created = roteiroService.patch(BigInteger.ONE, roteiro, oauth2Authentication);
    	Assertions.assertNotNull(created);
    	Assertions.assertNotNull(created.getId());
    	Assertions.assertNotNull(created.getTipoRoteiro());
    	Assertions.assertNotNull(created.getTipoProjeto());
    	Assertions.assertNotNull(created.getCnpjContabilidade());
    	Assertions.assertNotNull(created.getContabilidadeId());
    	Assertions.assertNotNull(created.getCnpjEmpresa());
    	Assertions.assertNotNull(created.getEmpresaId());
    	Assertions.assertEquals(created.getStatus(), (short) 5);
    	Assertions.assertNotNull(created.getUsuario());
    	Assertions.assertNotNull(created.getDataCriacao());
    	Assertions.assertNotNull(created.getDataAtualizacao());
    }    
    
    @Test
    public void dadoRoteiroDTO_quandoFinalizaProjeto_entaoOK() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = RoteiroDTO.builder()
    			.status((short) 7)
    			.checkList(true)
    			.nome("Pagamentos Possamai")
    		.build();
    	
    	RoteiroDTO created = roteiroService.patch(BigInteger.ONE, roteiro, oauth2Authentication);
    	Assertions.assertNotNull(created);
    	Assertions.assertNotNull(created.getId());
    	Assertions.assertNotNull(created.getNome());
    	Assertions.assertNotNull(created.getCheckList());
    	Assertions.assertNotNull(created.getTipoRoteiro());
    	Assertions.assertNotNull(created.getTipoProjeto());
    	Assertions.assertNotNull(created.getCnpjContabilidade());
    	Assertions.assertNotNull(created.getContabilidadeId());
    	Assertions.assertNotNull(created.getCnpjEmpresa());
    	Assertions.assertNotNull(created.getEmpresaId());
    	Assertions.assertNotNull(created.getStatus());
    	Assertions.assertNotNull(created.getUsuario());
    	Assertions.assertNotNull(created.getDataCriacao());
    	Assertions.assertNotNull(created.getDataAtualizacao());
    }
    
    @Test
    public void dadoRoteiroDTO_quandoFinalizaProjetoNomeRepetido_entaoLancaExecao() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = RoteiroDTO.builder()    		
    			.cnpjContabilidade("20000000000000")
        		.contabilidadeId(BigInteger.valueOf(1830))
        		.cnpjEmpresa("12123456712312")
        		.empresaId(BigInteger.valueOf(120))
        		.status((short) 1)
        		.urlArquivo("porenquanto")
        	.build();
    	RoteiroDTO created = roteiroService.salva(roteiro, oauth2Authentication);
    	
    	roteiro = RoteiroDTO.builder()
    			.tipoRoteiro("PAG")
    			.status((short) 5)
    		.build();
    	created = roteiroService.patch(BigInteger.valueOf(2), roteiro, oauth2Authentication);
    	roteiro = RoteiroDTO.builder()
    			.status((short) 7)
    			.checkList(true)
    			.nome("Pagamentos Possamai")
    		.build();
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		roteiroService.patch(BigInteger.valueOf(2), roteiro, oauth2Authentication);
    	});
    	
    }
	
    // CONTABILIDADE
    
    @Test 
    public void dadoRoteiroDTO_quandoIniciaProjetoCnpjContabilidadeNull_entaoLancaExecao() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = roteiro.toBuilder()
    			.cnpjContabilidade(null)
    		.build();
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		roteiroService.salva(roteiro, oauth2Authentication);
    	});
    }
    
    @Test 
    public void dadoRoteiroDTO_quandoIniciaProjetoContabilidadeIdNull_entaoLancaExecao() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = roteiro.toBuilder()
    			.contabilidadeId(null)
    		.build();
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		roteiroService.salva(roteiro, oauth2Authentication);
    	});
    }
    
    // EMPRESA 
    
    @Test 
    public void dadoRoteiroDTO_quandoIniciaProjetoCnpjEmpresaNull_entaoLancaExecao() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = roteiro.toBuilder()
    			.cnpjEmpresa(null)
    		.build();
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		roteiroService.salva(roteiro, oauth2Authentication);
    	});
    }
    
    @Test 
    public void dadoRoteiroDTO_quandoIniciaProjetoEmpresaIdNull_entaoLancaExecao() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = roteiro.toBuilder()
    			.empresaId(null)
    		.build();
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		roteiroService.salva(roteiro, oauth2Authentication);
    	});
    }
    
    // TIPO ROTEIRO
    
    @Test
    public void dadoRoteiroDTO_quandoAtualizaTipoRoteiroNull_entaoLancaExecao() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = RoteiroDTO.builder()    		
    			.cnpjContabilidade("20000000000000")
        		.contabilidadeId(BigInteger.valueOf(1830))
        		.cnpjEmpresa("12123456712312")
        		.empresaId(BigInteger.valueOf(120))
        		.status((short) 1)
        		.urlArquivo("porenquanto")
        	.build();
    	RoteiroDTO created = roteiroService.salva(roteiro, oauth2Authentication);
    	
    	roteiro = RoteiroDTO.builder()
    			.tipoRoteiro(null)
    			.status((short) 5)
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		roteiroService.patch(BigInteger.valueOf(2), roteiro, oauth2Authentication);
    	});
    }
    
    // NOME ROTEIRO
    
    @Test
    public void dadoRoteiroDTO_quandoAtualizaNomeNull_entaoLancaExecao() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	roteiro = RoteiroDTO.builder()    		
    			.cnpjContabilidade("20000000000000")
        		.contabilidadeId(BigInteger.valueOf(1830))
        		.cnpjEmpresa("12123456712312")
        		.empresaId(BigInteger.valueOf(120))
        		.status((short) 1)
        		.urlArquivo("porenquanto")
        	.build();
    	RoteiroDTO created = roteiroService.salva(roteiro, oauth2Authentication);
    	
    	roteiro = RoteiroDTO.builder()
    			.tipoRoteiro("PAG")
    			.status((short) 5)
    		.build();
    	created = roteiroService.patch(BigInteger.valueOf(2), roteiro, oauth2Authentication);
    	roteiro = RoteiroDTO.builder()
    			.status((short) 7)
    			.checkList(true)
    			.nome(null)
    		.build();
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		roteiroService.patch(BigInteger.valueOf(2), roteiro, oauth2Authentication);
    	});
    }
    
}
