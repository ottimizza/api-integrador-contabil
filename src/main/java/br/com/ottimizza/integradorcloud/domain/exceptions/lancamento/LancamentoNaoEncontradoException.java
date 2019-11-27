package br.com.ottimizza.integradorcloud.domain.exceptions.lancamento;

public class LancamentoNaoEncontradoException extends Exception {

    static final long serialVersionUID = 1L;

    public LancamentoNaoEncontradoException(String errorMessage) {
        super(errorMessage);
    }

}
