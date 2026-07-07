package com.barbearia.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.barbearia.security.JwtAuthenticationEntryPoint;
import com.barbearia.security.JwtAuthenticationFilter;

/**
 * Configuração central do Spring Security com JWT.
 *
 * Regras:
 *  - /auth/**              → público (login)
 *  - POST /api/clientes    → público (cadastro de novos clientes)
 *  - OPTIONS /**           → público (preflight de CORS do navegador)
 *  - qualquer outra rota   → exige token JWT válido (Authorization: Bearer ...)
 *
 * Sessão STATELESS: a API não guarda sessão; toda requisição se autentica pelo token.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint entryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, JwtAuthenticationEntryPoint entryPoint) {
        this.jwtFilter = jwtFilter;
        this.entryPoint = entryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // API REST stateless não usa formulário/sessão, então CSRF é desnecessário
            .csrf(AbstractHttpConfigurer::disable)
            // Usa a configuração de CORS definida abaixo (libera o React)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Sem sessão em servidor — cada request traz seu próprio token
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Define quais rotas são públicas e quais exigem autenticação
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/servicos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comments").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clientes/insert").permitAll()
                // Imagens estáticas — públicas para a tag <img> carregar sem token
                .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                // Funcionários liberados sem token (foto + dados da "Nossa Equipe" e upload)
                .requestMatchers("/api/funcionarios/**").permitAll()
                .requestMatchers("/api/funcionarios").permitAll()
                .anyRequest().authenticated())
            // Resposta 401 em JSON quando faltar token válido
            .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
            // Insere o filtro JWT antes do filtro padrão de usuário/senha
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuração de CORS usada pelo Spring Security.
     * allowedOriginPatterns("*") + allowCredentials(true) permite o React
     * de qualquer origem em desenvolvimento (restrinja em produção).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
