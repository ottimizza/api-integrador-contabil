package br.com.ottimizza.integradorcloud.services;

import java.math.BigInteger;
import java.time.LocalDate;

import com.amazonaws.auth.policy.Principal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.integradorcloud.IntegradorCloudApplication;
import br.com.ottimizza.integradorcloud.domain.dtos.LivroCaixaDTO;

@RunWith(SpringRunner.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = IntegradorCloudApplication.class)
class LivroCaixaServiceTest {
 
    public static final String ADMINISTRATOR = "administrator@ottimizza.com.br";
    public static final String ACCOUNTANT = "accountant@ottimizza.com.br";
    public static final String CUSTOMER = "customer@ottimizza.com.br";
    
    @Mock
    private Principal principal;

    @Mock
    private OAuth2Authentication oauth2Authentication;
    
    @Autowired
    LivroCaixaService livroCaixaService;
    
    @Test
    @Order(1)
    public void dadoLivroCaixaDTO_quandoSalvaLivroCaixa_entaoOk() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);

        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade("20000000000000")
            .cnpjEmpresa("00810852999156")
            .descricao("TESTEOTT livroCaixa")
            .dataMovimento(LocalDate.of(2021, 01, 01))
            .valorOriginal(2000.0)
            .valorPago(2000.0)
            .valorFinal(2000.0)
            .tipoMovimento("PAG")
            .origem(0)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(BigInteger.ONE)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();

        LivroCaixaDTO retorno = livroCaixaService.salva(livroCaixa, oauth2Authentication);
    	Assertions.assertNotNull(retorno);
    	Assertions.assertNotNull(retorno.getId());
    	Assertions.assertNotNull(retorno.getCnpjContabilidade());
    	Assertions.assertNotNull(retorno.getCnpjEmpresa());
        Assertions.assertNotNull(retorno.getDescricao());
        Assertions.assertNotNull(retorno.getDataMovimento());
        Assertions.assertNotNull(retorno.getValorFinal());
        Assertions.assertNotNull(retorno.getValorPago());
        Assertions.assertNotNull(retorno.getValorOriginal());
        Assertions.assertNotNull(retorno.getTipoMovimento());
        Assertions.assertNotNull(retorno.getOrigem());
        Assertions.assertNotNull(retorno.getIntegradoContabilidade());
        Assertions.assertNotNull(retorno.getStatus());
        Assertions.assertNotNull(retorno.getBancoId());
        Assertions.assertNotNull(retorno.getCategoriaId());
        Assertions.assertNotNull(retorno.getCriadoPor());
        Assertions.assertNotNull(retorno.getDataCriacao());
    }

    @Test
    @Order(2)
    public void dadoLivroCaixaDTO_quandoAtualizaLivroCaixa_entaoOk() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);

        LivroCaixaDTO livroCaixaDTO = LivroCaixaDTO.builder()
                .descricao("Alterando TESTEOTT")
                .complemento("Alterando TESTEOTT")
                .valorOriginal(20.0)
                .status(Short.valueOf("0"))
            .build();

        LivroCaixaDTO retorno = livroCaixaService.patch(BigInteger.ONE, livroCaixaDTO);
        Assertions.assertNotNull(retorno);
    	Assertions.assertNotNull(retorno.getId());
    	Assertions.assertNotNull(retorno.getCnpjContabilidade());
    	Assertions.assertNotNull(retorno.getCnpjEmpresa());
        Assertions.assertEquals("Alterando TESTEOTT", retorno.getDescricao());
        Assertions.assertNotNull(retorno.getDataMovimento());
        Assertions.assertNotNull(retorno.getValorFinal());
        Assertions.assertNotNull(retorno.getValorPago());
        Assertions.assertEquals(20.0, retorno.getValorOriginal());
        Assertions.assertNotNull(retorno.getTipoMovimento());
        Assertions.assertNotNull(retorno.getOrigem());
        Assertions.assertNotNull(retorno.getIntegradoContabilidade());
        Assertions.assertEquals(Short.valueOf("0"), retorno.getStatus());
        Assertions.assertNotNull(retorno.getBancoId());
        Assertions.assertNotNull(retorno.getCategoriaId());
        Assertions.assertNotNull(retorno.getCriadoPor());
        Assertions.assertNotNull(retorno.getDataCriacao());

    }

    @Test
    public void dadoLivroCaixaDTO_quandoSalvaLivroCaixaCnpjContabilidadeNull_entaoLancaExecao() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);

        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade(null)
            .cnpjEmpresa("00810852999156")
            .descricao("TESTEOTT livroCaixa")
            .dataMovimento(LocalDate.of(2021, 01, 01))
            .valorOriginal(2000.0)
            .valorPago(2000.0)
            .valorFinal(2000.0)
            .tipoMovimento("PAG")
            .origem(0)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(BigInteger.ONE)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            livroCaixaService.salva(livroCaixa, oauth2Authentication);
        });
    }

    @Test
    public void dadoLivroCaixaDTO_quandoSalvaLivroCaixaCnpjEmpresaNull_entaoLancaExecao() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade("20000000000000")
            .cnpjEmpresa(null)
            .descricao("TESTEOTT livroCaixa")
            .dataMovimento(LocalDate.of(2021, 01, 01))
            .valorOriginal(2000.0)
            .valorPago(2000.0)
            .valorFinal(2000.0)
            .tipoMovimento("PAG")
            .origem(0)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(BigInteger.ONE)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            livroCaixaService.salva(livroCaixa, oauth2Authentication);
        });
    }

    @Test
    public void dadoLivroCaixaDTO_quandoSalvaLivroCaixaDataMovimentoNull_entaoLancaExecao() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade("20000000000000")
            .cnpjEmpresa("00810852999156")
            .descricao("TESTEOTT livroCaixa")
            .dataMovimento(null)
            .valorOriginal(2000.0)
            .valorPago(2000.0)
            .valorFinal(2000.0)
            .tipoMovimento("PAG")
            .origem(0)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(BigInteger.ONE)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            livroCaixaService.salva(livroCaixa, oauth2Authentication);
        });
    }

    @Test
    public void dadoLivroCaixaDTO_quandoSalvaLivroCaixaBancoIdNull_entaoLancaExecao() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade("20000000000000")
            .cnpjEmpresa("00810852999156")
            .descricao("TESTEOTT livroCaixa")
            .dataMovimento(LocalDate.of(2021, 01, 01))
            .valorOriginal(2000.0)
            .valorPago(2000.0)
            .valorFinal(2000.0)
            .tipoMovimento("PAG")
            .origem(0)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(null)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            livroCaixaService.salva(livroCaixa, oauth2Authentication);
        });
    }

    @Test
    public void dadoLivroCaixaDTO_quandoSalvaLivroCaixaDescricaoNull_entaoLancaExecao() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade("20000000000000")
            .cnpjEmpresa("00810852999156")
            .descricao(null)
            .dataMovimento(LocalDate.of(2021, 01, 01))
            .valorOriginal(2000.0)
            .valorPago(2000.0)
            .valorFinal(2000.0)
            .tipoMovimento("PAG")
            .origem(0)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(BigInteger.ONE)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            livroCaixaService.salva(livroCaixa, oauth2Authentication);
        });
    }

    @Test
    public void dadoLivroCaixaDTO_quandoSalvaLivroCaixaValorOriginalNull_entaoLancaExecao() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);
        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade("20000000000000")
            .cnpjEmpresa("00810852999156")
            .descricao("TESTEOTT livroCaixa")
            .dataMovimento(LocalDate.of(2021, 01, 01))
            .valorOriginal(null)
            .valorPago(null)
            .valorFinal(null)
            .tipoMovimento("PAG")
            .origem(0)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(BigInteger.ONE)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            livroCaixaService.salva(livroCaixa, oauth2Authentication);
        });
    }

    @Test
    public void dadoLivroCaixaDTO_quandoAtualizaLivroCaixaOrigemUm_entaoLancaExecao() throws Exception {
        Mockito.when(oauth2Authentication.getName()).thenReturn(ADMINISTRATOR);

        LivroCaixaDTO livroCaixa = LivroCaixaDTO.builder()
            .cnpjContabilidade("20000000000000")
            .cnpjEmpresa("00810852999156")
            .descricao("TESTEOTT livroCaixa")
            .dataMovimento(LocalDate.of(2021, 01, 01))
            .valorOriginal(2000.0)
            .valorPago(2000.0)
            .valorFinal(2000.0)
            .tipoMovimento("PAG")
            .origem(1)
            .integradoContabilidade(false)
            .status(Short.valueOf("1"))
            .bancoId(BigInteger.ONE)
            .categoriaId(BigInteger.ONE)
            .criadoPor("Robo de testes")
        .build();

        LivroCaixaDTO retorno = livroCaixaService.salva(livroCaixa, oauth2Authentication);

        LivroCaixaDTO livroCaixaDTO = LivroCaixaDTO.builder()
                .descricao("Alterando TESTEOTT")
            .build();

         Assertions.assertThrows(IllegalArgumentException.class, () -> {
            livroCaixaService.patch(retorno.getId(), livroCaixaDTO);
         });
        
    }

}
