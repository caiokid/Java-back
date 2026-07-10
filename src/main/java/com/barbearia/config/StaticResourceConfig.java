package com.barbearia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serve a pasta física "images/" (na raiz do projeto) pela URL /images/**.
 *
 * Equivalente ao express.static('images') do Node:
 *   um arquivo images/joao.jpg fica acessível em http://localhost:8080/images/joao.jpg
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                // "file:images/" → caminho relativo à raiz onde a app roda.
                // A barra final é obrigatória para o Spring tratar como diretório.
                .addResourceLocations("file:images/");
    }
}
