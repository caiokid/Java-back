package com.barbearia.model;

/**
 * Modelo da tabela "tb_agendados" (agendamentos).
 * Colunas: id_agenda (PK), id_usuario, id_funcionario, id_servicos,
 *          horario, dia, horario_fim, status.
 *
 * Os campos id_* são chaves estrangeiras (referenciam clientes, funcionarios e servicos).
 */
public class Agendamento {

    private Integer id_agenda;
    private Integer id_usuario;      // FK → clientes
    private Integer id_funcionario;  // FK → funcionarios
    private Integer id_servicos;     // FK → servicos
    private String horario;          // Horário de início (varchar)
    private String dia;              // Dia do agendamento (varchar)
    private String horario_fim;      // Horário de término (varchar)
    private Integer status;          // 0 = pendente, 1 = confirmado, etc.

    public Agendamento() {}

    public Integer getId_agenda() { return id_agenda; }
    public void setId_agenda(Integer id_agenda) { this.id_agenda = id_agenda; }

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }

    public Integer getId_funcionario() { return id_funcionario; }
    public void setId_funcionario(Integer id_funcionario) { this.id_funcionario = id_funcionario; }

    public Integer getId_servicos() { return id_servicos; }
    public void setId_servicos(Integer id_servicos) { this.id_servicos = id_servicos; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getHorario_fim() { return horario_fim; }
    public void setHorario_fim(String horario_fim) { this.horario_fim = horario_fim; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
