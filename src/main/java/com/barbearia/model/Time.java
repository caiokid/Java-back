package com.barbearia.model;

/**
 * Modelo da tabela "times" (horários disponíveis/agenda).
 * Colunas: id_time (PK), mes, horario_inicio, horario_fim, id_funcionario, id_cliente.
 */
public class Time {

    private Integer id_time;
    private String mes;            // Mês/dia de referência (varchar no banco)
    private String horario_inicio; // Início do horário (varchar)
    private String horario_fim;    // Fim do horário (varchar)
    private Integer id_funcionario; // Funcionário dono do horário
    private Integer id_cliente;     // Cliente que reservou (pode ser null se livre)

    public Time() {}

    public Integer getId_time() { return id_time; }
    public void setId_time(Integer id_time) { this.id_time = id_time; }

    public String getMes() { return mes; }
    public void setMes(String mes) { this.mes = mes; }

    public String getHorario_inicio() { return horario_inicio; }
    public void setHorario_inicio(String horario_inicio) { this.horario_inicio = horario_inicio; }

    public String getHorario_fim() { return horario_fim; }
    public void setHorario_fim(String horario_fim) { this.horario_fim = horario_fim; }

    public Integer getId_funcionario() { return id_funcionario; }
    public void setId_funcionario(Integer id_funcionario) { this.id_funcionario = id_funcionario; }

    public Integer getId_cliente() { return id_cliente; }
    public void setId_cliente(Integer id_cliente) { this.id_cliente = id_cliente; }
}
