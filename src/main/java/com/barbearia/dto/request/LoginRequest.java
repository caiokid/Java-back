package com.barbearia.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de ENTRADA do login — representa o corpo JSON recebido em POST /auth/login.
 *
 * Ex: { "email": "fulano@email.com", "senha": "123", "tipo": "cliente" }
 *
 * "tipo" é opcional: ausente ou "cliente" → login de cliente; "funcionario" → login de funcionário.
 */
public record LoginRequest(

        @NotBlank(message = "O email é obrigatório")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha,

        String tipo
) {}
