package com.barbearia.exception;

/**
 * Lançada quando um registro não existe no banco (ID inválido).
 * O ApiExceptionHandler converte em resposta HTTP 404.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
