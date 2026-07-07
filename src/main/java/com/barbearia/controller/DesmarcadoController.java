package com.barbearia.controller;

import com.barbearia.model.Desmarcado;
import com.barbearia.service.DesmarcadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * API REST do histórico de cancelamentos (tabela desmarcados).
 */
@RestController
@RequestMapping("/api/desmarcados")
public class DesmarcadoController {

    private final DesmarcadoService service;

    // Injeção de dependência via construtor (o Spring instancia pra você)
    public DesmarcadoController(DesmarcadoService service) {
        this.service = service;
    }

    // GET /api/desmarcados?funcionario=João
    @GetMapping
    public List<Desmarcado> listar(@RequestParam(required = false) String funcionario) {
        return service.listar(funcionario);
    }

    // PUT /api/desmarcados/5/visto → marca a notificação como lida (views = 1)
    @PutMapping("/{id}/visto")
    public Map<String, String> marcarComoVisto(@PathVariable Integer id) {
        service.marcarComoVisto(id);
        return Map.of("mensagem", "Cancelamento marcado como visto");
    }

    // DELETE /api/desmarcados/5
    @DeleteMapping("/{id}")
    public Map<String, String> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return Map.of("mensagem", "Registro removido com sucesso");
    }
}
