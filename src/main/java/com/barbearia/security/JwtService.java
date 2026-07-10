package com.barbearia.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Responsável por GERAR e VALIDAR os tokens JWT.
 * A chave e o tempo de expiração vêm do application.properties.
 */
@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracaoMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") long expiracaoMs) {
        // Deriva a chave de assinatura HMAC a partir do segredo configurado
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracaoMs = expiracaoMs;
    }

    /**
     * Gera um token assinado contendo o email (subject) e dados extras (claims):
     * tipo de usuário, id e nome — úteis para o front e para autorização.
     */
    public String gerarToken(String email, String tipo, Integer id, String nome) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expiracaoMs);

        return Jwts.builder()
                .subject(email)
                .claim("tipo", tipo)
                .claim("id", id)
                .claim("nome", nome)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(chave)
                .compact();
    }

    /**
     * Valida a assinatura e a expiração do token, retornando seus claims.
     * Lança JwtException (ou subclasse) se o token for inválido/expirado.
     */
    public Claims validarExtrair(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Integer extrairId(String token) {
        return validarExtrair(token).get("id", Integer.class);
    }


    /** Atalho para ler o email (subject) de um token válido. */
    public String extrairEmail(String token) {
        return validarExtrair(token).getSubject();
    }
}
