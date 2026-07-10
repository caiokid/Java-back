package com.barbearia.exception;

/**
 * Lançada quando email ou senha estão incorretos no login.
 * O ApiExceptionHandler converte em resposta HTTP 401 (Unauthorized).
 */
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
