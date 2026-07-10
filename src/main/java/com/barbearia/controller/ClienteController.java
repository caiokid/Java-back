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

import com.barbearia.model.Cliente;
import com.barbearia.service.ClienteService;

import jakarta.validation.Valid;

/**
 * API REST de clientes.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;

    // Injeção de dependência via construtor (o Spring instancia pra você)
    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // GET /api/clientes
    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    // GET /api/clientes/5
    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    // POST /api/clientes
    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente criar(@Valid @RequestBody Cliente cliente) {
        return service.salvar(cliente);
    }

    // PUT /api/clientes/5
    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Integer id,
                             @Valid @RequestBody Cliente cliente) {
        return service.atualizar(id, cliente);
    }

    // DELETE /api/clientes/5
    @DeleteMapping("/{id}")
    public Map<String, String> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return Map.of("mensagem", "Cliente removido com sucesso");
    }
}
