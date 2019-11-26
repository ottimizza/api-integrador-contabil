package br.com.ottimizza.integradorcloud.services;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.Principal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.integradorcloud.IntegradorCloudApplication;
import br.com.ottimizza.integradorcloud.domain.dtos.arquivo.ArquivoDTO;
import br.com.ottimizza.integradorcloud.domain.dtos.lancamento.LancamentoDTO;
import br.com.ottimizza.integradorcloud.domain.models.Lancamento;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IntegradorCloudApplication.class) // @formatter:off
class LancamentoServiceTest {

    public static final String ADMINISTRATOR = "administrator@ottimizza.com.br";
    public static final String ACCOUNTANT = "accountant@ottimizza.com.br";
    public static final String CUSTOMER = "customer@ottimizza.com.br";

    @Mock
    private Principal principal;

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
                .nome("Lançamentos 10/2019.xlsx")
                .cnpjEmpresa("99097492000142")
                .cnpjContabilidade("82910893000177")
            .build()
        )
        .tipoPlanilha("MOVIMENTO")
        .tipoLancamento(Lancamento.Tipo.PAGAMENTO)
        .tipoMovimento("CTB") 
        .contaMovimento("")
        .contaContraPartida("")
        .valorOriginal(422.25)
        .valorPago(0.0)
        .valorJuros(0.0)
        .valorDesconto(0.0)
        .valorMulta(0.0)
        .complemento01("REF 10/2019")
        .complemento02("SERVIÇO")
        .complemento03("À VISTA")
        .complemento04("")
        .complemento05("")
        .cnpjEmpresa("99097492000142")
        .cnpjContabilidade("82910893000177")
        .idRoteiro("h4ub5ijfdASd")
    .build();

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamento_entaoOK() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        LancamentoDTO created = lancamentoService.criar(lancamento, principal);
        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
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
            lancamentoService.criar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoCnpjEmpresaBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .cnpjEmpresa("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.criar(lancamento, principal);
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
            lancamentoService.criar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoCnpjContabilidadeBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .cnpjContabilidade("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.criar(lancamento, principal);
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
            lancamentoService.criar(lancamento, principal);
        });
	}

    @Test
    public void dadoLancamentoDTO_quandoSalvaLancamentoIdRoteiroBranco_entaoThrowIllegalArgumentsException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        lancamento = lancamento.toBuilder()
                .idRoteiro("")
            .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            lancamentoService.criar(lancamento, principal);
        });
	}

}
