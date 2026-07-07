package com.barbearia.dto.request;

public record AgendamentoRequest(
    String horario,          // horário início
    String dia,              // data
    String id_servicos,      // id do serviço
    String id_funcionario,   // id do funcionário
    String duration          // duração do serviço
) {}