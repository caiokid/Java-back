package com.barbearia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barbearia.dto.request.AgendamentoRequest;
import com.barbearia.dto.response.AgendamentoResponse;
import com.barbearia.model.Agendamento;
import com.barbearia.security.JwtService;
import com.barbearia.service.AgendamentoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * API REST dos agendamentos (tabela tb_agendados).
 */
@RestController
@RequestMapping("/marcar/horario")
public class AgendamentoController {

    private final AgendamentoService service;

    private final JwtService jwtService;

    // Injeção de dependência via construtor (o Spring instancia pra você)
    public AgendamentoController(AgendamentoService service, JwtService jwtService) {
        this.service = service; 
         this.jwtService = jwtService;
    }


    @GetMapping
    public List<Agendamento> listar(
            @RequestParam(name = "funcionario", required = false) Integer idFuncionario,
            @RequestParam(name = "usuario", required = false) Integer idUsuario) {
        return service.listar(idFuncionario, idUsuario);
    }

    // GET /api/agendamentos/5
    @GetMapping("/{id}")
    public Agendamento buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    // POST /api/agendamentos
    @PostMapping("/confirmar")
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponse criar(@Valid @RequestBody AgendamentoRequest req,  HttpServletRequest request){
 
      String token = request.getHeader("Authorization").substring(7);
       
      Integer userId = jwtService.extrairId(token);

      return service.salvar(req, userId);
    }

}
