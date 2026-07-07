package com.barbearia.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.model.Comentarios;
import com.barbearia.service.ComentarioService;

/**
 * API REST dos serviços oferecidos pela barbearia.
 */
@RestController
@RequestMapping("/api/comments")
public class ComentariosController{

    private final ComentarioService service;

    public ComentariosController(ComentarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Comentarios> listar() {
        return service.list();
    }
}
