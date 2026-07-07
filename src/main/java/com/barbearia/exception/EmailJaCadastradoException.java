package com.barbearia.exception;

/**
 * Lançada ao tentar cadastrar um email que já existe.
 * O ApiExceptionHandler converte em resposta HTTP 409 (Conflict).
 */
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
