package com.barbearia.dto.response;

/**
 * DTO de saída do agendamento — o que a API devolve após criar.
 */
public record AgendamentoResponse(
    String message,
    String startTime,
    String endTime,
    String service,
    String duration
) {}