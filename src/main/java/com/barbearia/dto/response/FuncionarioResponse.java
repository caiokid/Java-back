package com.barbearia.dto.response;

/**
 * DTO de SAÍDA do funcionário — o que a API devolve para o front
 * (ex: seção "Nossa Equipe"). Inclui a URL completa da imagem.
 *
 * A senha NUNCA aparece aqui.
 */
public record FuncionarioResponse(
        String nome_funcionario,
        String email,
        String imageUrl
) {}