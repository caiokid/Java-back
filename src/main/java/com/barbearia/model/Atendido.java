package com.barbearia.model;

/**
 * Modelo da tabela "atendidos" (histórico de atendimentos concluídos).
 *
 * OBS: colunas inferidas seguindo o padrão de "desmarcados".
 * Ajuste aqui e no AtendidoDAO se sua tabela for diferente.
 */
public class Atendido {

    private int id_atendidos;
    private int id_agenda;      // Referência ao agendamento original
    private String usuario;
    private String funcionario;
    private String servicos;
    private String horario;

    public Atendido() {}

    public int getId_atendidos() { return id_atendidos; }
    public void setId_atendidos(int id_atendidos) { this.id_atendidos = id_atendidos; }

    public int getId_agenda() { return id_agenda; }
    public void setId_agenda(int id_agenda) { this.id_agenda = id_agenda; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getFuncionario() { return funcionario; }
    public void setFuncionario(String funcionario) { this.funcionario = funcionario; }

    public String getServicos() { return servicos; }
    public void setServicos(String servicos) { this.servicos = servicos; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
}
