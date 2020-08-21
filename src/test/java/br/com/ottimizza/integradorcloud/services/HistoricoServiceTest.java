package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.security.Principal;
import java.sql.Date;
import java.time.Instant;


import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.integradorcloud.IntegradorCloudApplication;
import br.com.ottimizza.integradorcloud.domain.dtos.HistoricoDTO;

@RunWith(SpringRunner.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = IntegradorCloudApplication.class)
class HistoricoServiceTest {

	public static final String ADMINISTRATOR = "administrator@ottimizza.com.br";
    public static final String ACCOUNTANT = "accountant@ottimizza.com.br";
    public static final String CUSTOMER = "customer@ottimizza.com.br";
    
    @Mock
    private Principal principal;

    @Mock
    private OAuth2Authentication oauth2Authentication;

    @Autowired
    HistoricoService historicoService;
    
    HistoricoDTO historico = HistoricoDTO.builder()
    	.dataCriacao(Date.from(Instant.now()))
    	.dataAtualizacao(Date.from(Instant.now()))
    	.contaMovimento("12056")
    	.cnpjContabilidade("20000000000000")
    	.cnpjEmpresa("01715107999199")
    	.historico("$ IGUAL A ${complemento01} ${nenhum} ${nenhum} ")
    	.idRoteiro("a0A1C000011YYGZ")
    	.tipoLancamento((short) 1) 
    .build();
    
    @Test
    @Order(1)
    public void dadoHistoricoDTO_quandoSalvaHistorico_entaoOK() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	HistoricoDTO created = historicoService.salvar(historico, oauth2Authentication);
    	Assertions.assertNotNull(created);
    	Assertions.assertNotNull(created.getId());
    	Assertions.assertNotNull(created.getContaMovimento());
    	Assertions.assertNotNull(created.getHistorico());
    	Assertions.assertNotNull(created.getCnpjEmpresa());
    	Assertions.assertNotNull(created.getCnpjContabilidade());
    	Assertions.assertNotNull(created.getTipoLancamento());
    	Assertions.assertNotNull(created.getIdRoteiro());
    	Assertions.assertNotNull(created.getDataCriacao());
    	Assertions.assertNotNull(created.getDataAtualizacao());
    }
    
    @Test
    @Order(2)
    public void dadoHistoricoDTO_quandoAtualizaHistorico_entaoOK() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	
    	// Alterando historico
    	historico.setHistorico("$ CONTEM ${competenciaAnterior} ${nenhum} ${nenhum}");
    	
    	HistoricoDTO created = historicoService.atualizar(BigInteger.ONE, historico, oauth2Authentication);
    	Assertions.assertNotNull(created);
    	Assertions.assertNotNull(created.getId());
    	Assertions.assertNotNull(created.getContaMovimento());
    	Assertions.assertEquals(created.getHistorico(),"$ CONTEM ${competenciaAnterior} ${nenhum} ${nenhum}");
    	Assertions.assertNotNull(created.getCnpjEmpresa());
    	Assertions.assertNotNull(created.getCnpjContabilidade());
    	Assertions.assertNotNull(created.getTipoLancamento());
    	Assertions.assertNotNull(created.getIdRoteiro());
    	Assertions.assertNotNull(created.getDataCriacao());
    	Assertions.assertNotNull(created.getDataAtualizacao());
    }
    
    // CONTA MOVIMENTO
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoContaMovimentoNull_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.contaMovimento(null)
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoContaMovimentoBranco_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.contaMovimento("")
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    // HISTORICO
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoHistoricoNull_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.historico(null)
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoHistoricoBranco_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.historico("")
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
   // CNPJ EMPRESA
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoCnpjEmpresaNull_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.cnpjEmpresa(null)
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoCnpjEmpresaBranco_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.cnpjEmpresa("")
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    // CNPJ CONTABILIDADE
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoCnpjContabilidadeNull_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.cnpjContabilidade(null)
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoCnpjContabilidadeBranco_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.cnpjContabilidade("")
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    // TIPO LANCAMENTO
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoTipoLancamentoNull_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.tipoLancamento(null)
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    // ID ROTEIRO
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoIdRoteiroNull_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.idRoteiro(null)
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
    
    @Test
    public void dadoHistoricoDTO_quandoSalvaHistoricoIdRoteiroBranco_entaoThrowIllegalArgumentsException() throws Exception {
    	Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
    	historico = historico.toBuilder()
    			.idRoteiro("")
    		.build();
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
            historicoService.salvar(historico, oauth2Authentication);
        });
    }
}
