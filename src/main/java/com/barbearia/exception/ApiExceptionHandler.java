package com.barbearia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Tratador GLOBAL de erros da API.
 * Captura as exceções lançadas pelos services e converte em respostas
 * JSON padronizadas — assim os controllers ficam limpos, sem try/catch.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /** Registro não encontrado → 404 */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> recursoNaoEncontrado(RecursoNaoEncontradoException e) {
        return Map.of("erro", e.getMessage());
    }

    /** Email duplicado → 409 Conflict */
    @ExceptionHandler(EmailJaCadastradoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> emailJaCadastrado(EmailJaCadastradoException e) {
        return Map.of("erro", e.getMessage());
    }

    /** Login inválido → 401 Unauthorized */
    @ExceptionHandler(CredenciaisInvalidasException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> credenciaisInvalidas(CredenciaisInvalidasException e) {
        return Map.of("erro", e.getMessage());
    }

    /** Regra de negócio violada (ex: senha ausente no cadastro) → 400 */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> argumentoInvalido(IllegalArgumentException e) {
        return Map.of("erro", e.getMessage());
    }

    /** Falha de validação do @Valid → 400 com a lista de campos inválidos */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> validacao(MethodArgumentNotValidException e) {
        Map<String, String> campos = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.put(erro.getField(), erro.getDefaultMessage()));
        return Map.of("erro", "Dados inválidos", "campos", campos);
    }

    /** Qualquer outro erro inesperado → 500 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> erroInterno(Exception e) {
        return Map.of("erro", "Erro interno: " + e.getMessage());
    }
}
