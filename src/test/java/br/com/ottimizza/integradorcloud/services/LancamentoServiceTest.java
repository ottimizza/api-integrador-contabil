package br.com.ottimizza.integradorcloud.services;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.security.Principal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.integradorcloud.IntegradorCloudApplication;
import br.com.ottimizza.integradorcloud.domain.dtos.ArquivoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = IntegradorCloudApplication.class) // @formatter:off
class LancamentoServiceTest {

    public static final String ADMINISTRATOR = "administrator@ottimizza.com.br";
    public static final String ACCOUNTANT = "accountant@ottimizza.com.br";
    public static final String CUSTOMER = "customer@ottimizza.com.br";

    @Mock
    private Principal principal;

    @Mock
    private OAuth2Authentication oauth2Authentication;

    @Autowired
    LancamentoService lancamentoService;

    LancamentoDTO lancamento = LancamentoDTO.builder()
        .dataMovimento(LocalDate.now())
        .documento("7835")
        .descricao("BRADESCO SEGUROS")
        .portador("BRADESCO")
        .centroCusto("")
        .arquivo(
            ArquivoDTO.builder()
                .nome("Lançamentos 10-2019.xlsx")
            .build()
        )
        .tipoPlanilha("MOVIMENTO")
        .tipoLancamento(Lancamento.Tipo.PAGAMENTO)
        .tipoMovimento("CTB") 
        .contaMovimento("")
        .contaContraPartida("")
        .valorOriginal(422.25)
        .valorPago(0.0)
        .valorJuros(null)
        .valorDesconto(null)
        .valorMulta(null)
        .complemento01("REF 10/2019")
        .complemento02("SERVIÇO")
        .complemento03("À VISTA")
        .complemento04("")
        .complemento05(null)
        .cnpjEmpresa("99097492000142")
        .cnpjContabilidade("82910893000177")
        .idRoteiro("h4ub5ijfdASd")
    .build();

    @Test
    @Order(1)
    public void dadoLancamentoDTO_quandoSalvaLancamento_entaoOK() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        LancamentoDTO created = lancamentoService.salvar(lancamento, principal);
        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
        Assertions.assertNotNull(created.getDocumento()); // DADOS
        Assertions.assertNotNull(created.getDescricao());
        Assertions.assertNotNull(created.getPortador()); 
        Assertions.assertNotNull(created.getCentroCusto());
        Assertions.assertNotNull(created.getContaMovimento()); // CONTAS CONTABEIS
        Assertions.assertNotNull(created.getContaContraPartida()); 
        Assertions.assertNotNull(created.getValorOriginal()); // VALORES
        Assertions.assertNotNull(created.getValorPago());
        Assertions.assertNotNull(created.getValorDesconto());
        Assertions.assertNotNull(created.getValorJuros());
        Assertions.assertNotNull(created.getValorMulta()); 
        Assertions.assertNotNull(created.getComplemento01());  // COMPLEMENTOS
        Assertions.assertNotNull(created.getComplemento02());
        Assertions.assertNotNull(created.getComplemento03());
        Assertions.assertNotNull(created.getComplemento04());
        Assertions.assertNotNull(created.getComplemento05());
	}
    
    @Test
    @Order(2)
    public void dadoLancamentoDTO_quandoAtualizaLancamento_entaoOK() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);

        // Altera Campos 
        LancamentoDTO lancamento = new LancamentoDTO();
        lancamento.setContaMovimento("2578");
        lancamento.setContaContraPartida("7645");

        LancamentoDTO created = lancamentoService.salvar(BigInteger.ONE, lancamento, principal);
        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
        Assertions.assertNotNull(created.getDocumento()); // DADOS
        Assertions.assertNotNull(created.getDescricao());
        Assertions.assertNotNull(created.getPortador());
        Assertions.assertNotNull(created.getCentroCusto());
        Assertions.assertNotNull(created.getContaMovimento()); // CONTAS CONTABEIS
        Assertions.assertNotNull(created.getContaContraPartida()); 
        Assertions.assertEquals(created.getContaMovimento(), "2578");
        Assertions.assertEquals(created.getContaContraPartida(), "7645");
        Assertions.assertNotNull(created.getValorOriginal()); // VALORES
        Assertions.assertNotNull(created.getValorPago());
        Assertions.assertNotNull(created.getValorDesconto());
        Assertions.assertNotNull(created.getValorJuros());
        Assertions.assertNotNull(created.getValorMulta()); 
        Assertions.assertNotNull(created.getComplemento01());  // COMPLEMENTOS
        Assertions.assertNotNull(created.getComplemento02());
        Assertions.assertNotNull(created.getComplemento03());
        Assertions.assertNotNull(created.getComplemento04());
        Assertions.assertNotNull(created.getComplemento05());
	}

    /** *********************************************************************************************************************
     * Buscar Lancamento por Id 
     * ******************************************************************************************************************  */
    @Test
    public void dadoIdLancamento_quandoBuscaLancamento_entaoOK() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        LancamentoDTO lancamento = lancamentoService.buscarPorId(BigInteger.ONE, principal);
        Assertions.assertNotNull(lancamento);
        Assertions.assertNotNull(lancamento.getId());
        Assertions.assertNotNull(lancamento.getDocumento()); // DADOS
        Assertions.assertNotNull(lancamento.getDescricao());
        Assertions.assertNotNull(lancamento.getPortador());
        Assertions.assertNotNull(lancamento.getCentroCusto());
        Assertions.assertNotNull(lancamento.getContaMovimento()); // CONTAS CONTABEIS
        Assertions.assertNotNull(lancamento.getContaContraPartida()); 
        Assertions.assertNotNull(lancamento.getValorOriginal()); // VALORES
        Assertions.assertNotNull(lancamento.getValorPago());
        Assertions.assertNotNull(lancamento.getValorDesconto());
        Assertions.assertNotNull(lancamento.getValorJuros());
        Assertions.assertNotNull(lancamento.getValorMulta()); 
        Assertions.assertNotNull(lancamento.getComplemento01());  // COMPLEMENTOS
        Assertions.assertNotNull(lancamento.getComplemento02());
        Assertions.assertNotNull(lancamento.getComplemento03());
        Assertions.assertNotNull(lancamento.getComplemento04());
        Assertions.assertNotNull(lancamento.getComplemento05());
	}
    
    /** *********************************************************************************************************************
     * Data Movimento
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoDataMovimentoNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .dataMovimento(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    /*@Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoDataMovimentoFuturo_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .dataMovimento(LocalDate.now().plusDays(1))
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}*/

    /** *********************************************************************************************************************
     * Tipo Planilha
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoTipoPlanilhaNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .tipoPlanilha(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoTipoPlanilhaBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .tipoPlanilha("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}
    
    /** *********************************************************************************************************************
     * Tipo Lancamento
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoTipoLancamentoNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .tipoLancamento(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoTipoLancamentoInvalido_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .tipoLancamento(Short.valueOf("88"))
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    /** *********************************************************************************************************************
     * Tipo Movimento
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoTipoMovimentoNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .tipoMovimento(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoTipoMovimentoBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .tipoMovimento("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    /** *********************************************************************************************************************
     * Valor Original
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoValorOriginalNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .valorOriginal(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoValorOriginalZero_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .valorOriginal(0.0)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoValorOriginalMenorZero_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .valorOriginal(-345.0)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    /** *********************************************************************************************************************
     * CNPJ Empresa
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoCnpjEmpresaNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .cnpjEmpresa(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoCnpjEmpresaBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .cnpjEmpresa("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    /** *********************************************************************************************************************
     * CNPJ Contabilidade
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoCnpjContabilidadeNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .cnpjContabilidade(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoCnpjContabilidadeBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .cnpjContabilidade("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    /** *********************************************************************************************************************
     * ID Roteiro
     * ******************************************************************************************************************  */
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoIdRoteiroNull_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .idRoteiro(null)
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoIdRoteiroBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .idRoteiro("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.salvar(lancamento, principal);
        });
	}
    
    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoArquivoCNPJVazio_entaoUsaCNPJLancamento() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);

        LancamentoDTO created =  lancamentoService.salvar(lancamento, principal);

        Assertions.assertEquals(lancamento.getCnpjContabilidade(), created.getCnpjContabilidade());
        Assertions.assertEquals(lancamento.getCnpjEmpresa(), created.getCnpjEmpresa());
	}
    
    
}