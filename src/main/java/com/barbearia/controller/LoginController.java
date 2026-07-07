package com.barbearia.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.dto.request.LoginRequest;
import com.barbearia.dto.response.LoginResponse;
import com.barbearia.service.AuthService;

import jakarta.validation.Valid;

/**
 * API de autenticação.
 *
 * POST /auth/login
 * Body: { "email": "...", "senha": "...", "tipo": "cliente" | "funcionario" }
 *
 * 200 → { "token": "...", "tipo": "...", "id": ..., "nome": "...", "email": "...", "message": "Login realizado com sucesso!" }
 * 401 → { "erro": "Email ou senha inválidos" }
 *
 * O front-end deve guardar o token e enviá-lo nas próximas requisições:
 *   Authorization: Bearer <token>
 */
@RestController
@RequestMapping("/auth/login")
public class LoginController {

    private final AuthService service;

    // Injeção de dependência via construtor (o Spring instancia pra você)
    public LoginController(AuthService service) {
        this.service = service;
    }

    // POST /auth/login
    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }
}
