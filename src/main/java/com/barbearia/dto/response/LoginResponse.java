package com.barbearia.dto.response;

/**
 * DTO de SAÍDA do login — o que a API devolve após autenticar com sucesso.
 *
 * O front-end (React) deve guardar o "token" e enviá-lo nas próximas
 * requisições no cabeçalho: Authorization: Bearer <token>.
 *
 * A senha NUNCA aparece aqui.
 */
public record LoginResponse(
        String token,    // Token JWT assinado
        String tipo,     // "cliente" ou "funcionario"
        Integer id,      // ID do usuário autenticado
        String nome,     // Nome do usuário
        String email ,    // Email do usuário
        String message     // Mensagem de sucesso
) {

public LoginResponse(String token, String tipo, Integer id, String nome, String email) {
         this(token, tipo, id, nome, email, "Login realizado com sucesso!");
}
}
