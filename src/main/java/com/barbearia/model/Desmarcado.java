package com.barbearia.model;

/**
 * Modelo da tabela "desmarcados" (agendamentos cancelados).
 * Colunas: id_desmarcados (PK), id_agenda, usuario, funcionario,
 *          servicos, horario, status (smallint), views (smallint).
 */
public class Desmarcado {

    private int id_desmarcados;
    private int id_agenda;      // Referência ao agendamento original
    private String usuario;
    private String funcionario;
    private String servicos;
    private String horario;
    private int status;
    private int views;          // 0 = cancelamento ainda não visto, 1 = visto

    public Desmarcado() {}

    public int getId_desmarcados() { return id_desmarcados; }
    public void setId_desmarcados(int id_desmarcados) { this.id_desmarcados = id_desmarcados; }

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

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
}
