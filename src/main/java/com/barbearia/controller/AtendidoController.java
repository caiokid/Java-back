package com.barbearia.controller;

import com.barbearia.model.Atendido;
import com.barbearia.service.AtendidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * API REST do histórico de atendimentos concluídos (tabela atendidos).
 * Para CONCLUIR um atendimento use: PUT /api/agendamentos/{id}/concluir
 */
@RestController
@RequestMapping("/api/atendidos")
public class AtendidoController {

    private final AtendidoService service;

    // Injeção de dependência via construtor (o Spring instancia pra você)
    public AtendidoController(AtendidoService service) {
        this.service = service;
    }

    // GET /api/atendidos?funcionario=João
    @GetMapping
    public List<Atendido> listar(@RequestParam(required = false) String funcionario) {
        return service.listar(funcionario);
    }

    // DELETE /api/atendidos/5
    @DeleteMapping("/{id}")
    public Map<String, String> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return Map.of("mensagem", "Registro removido com sucesso");
    }
}
