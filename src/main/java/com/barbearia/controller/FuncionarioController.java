package com.barbearia.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.dto.response.FuncionarioResponse;
import com.barbearia.model.Funcionario;
import com.barbearia.service.FuncionarioService;

/**
 * API REST de funcionários (barbeiros).
 */
@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    // Injeção de dependência via construtor (o Spring instancia pra você)
    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    // GET /api/funcionarios → lista com a URL da imagem (alimenta "Nossa Equipe")
    @GetMapping
    public List<FuncionarioResponse> listar() {
        return service.listar();
    }

    @GetMapping("/employs")
    public List<Funcionario> listarEmpl() {
        return service.listarEmploys();
    }


    // GET /api/funcionarios/5
    @GetMapping("/{id}")
    public FuncionarioResponse buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }
}