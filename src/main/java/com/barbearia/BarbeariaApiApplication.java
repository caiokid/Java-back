package com.barbearia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 * Rode com: mvn spring-boot:run
 * A API sobe em http://localhost:8080 com Tomcat embutido.
 */
@SpringBootApplication
public class BarbeariaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarbeariaApiApplication.class, args);
    }
}
