package com.barbearia.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.model.Servico;
import com.barbearia.service.ServicoService;

import jakarta.validation.Valid;

/**
 * API REST dos serviços oferecidos pela barbearia.
 */
@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService service;

    // Injeção de dependência via construtor (o Spring instancia pra você)
    public ServicoController(ServicoService service) {
        this.service = service;
    }

    // GET /api/servicos
    @GetMapping
    public List<Servico> listar() {
        return service.listar();
    }

    // GET /api/servicos
    @GetMapping("/select/{id}")
    public List<Servico> listar(@PathVariable Integer id) {
        return service.listar();
    }

    @GetMapping("/{id}")
    public List<Servico> listarId(@PathVariable Integer id) {
        return service.listarId(id);
    }


 

    // POST /api/servicos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Servico criar(@Valid @RequestBody Servico servico) {
        return service.salvar(servico);
    }

    // PUT /api/servicos/5
    @PutMapping("/{id}")
    public Servico atualizar(@PathVariable Integer id,
                             @Valid @RequestBody Servico servico) {
        return service.atualizar(id, servico);
    }

    // DELETE /api/servicos/5
    @DeleteMapping("/{id}")
    public Map<String, String> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return Map.of("mensagem", "Serviço removido com sucesso");
    }
}
