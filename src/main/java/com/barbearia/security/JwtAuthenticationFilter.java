package com.barbearia.security;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro executado UMA vez por requisição.
 * Lê o cabeçalho "Authorization: Bearer <token>", valida o JWT e,
 * se válido, registra o usuário autenticado no contexto do Spring Security.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Só processa quando há um token no formato esperado
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtService.validarExtrair(token);
                String email = claims.getSubject();
                String tipo = claims.get("tipo", String.class);

                // Cria a autoridade ROLE_CLIENTE ou ROLE_FUNCIONARIO a partir do "tipo"
                var autoridade = new SimpleGrantedAuthority("ROLE_" + tipo.toUpperCase());
                var auth = new UsernamePasswordAuthenticationToken(email, null, List.of(autoridade));

                // Marca a requisição como autenticada
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException | IllegalArgumentException e) {
                // Token inválido/expirado: não autentica.
                // A requisição seguirá e será barrada pelo SecurityConfig (401) se a rota exigir login.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
