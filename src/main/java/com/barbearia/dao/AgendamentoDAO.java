package com.barbearia.dao;

import com.barbearia.model.Agendamento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO da tabela "tb_agendados" usando JdbcTemplate.
 */
@Repository
public class AgendamentoDAO {

    private final JdbcTemplate jdbc;

    public AgendamentoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Agendamento> mapper = (rs, linha) -> {
        Agendamento a = new Agendamento();
        a.setId_agenda(rs.getInt("id_agenda"));
        a.setId_usuario((Integer) rs.getObject("id_usuario"));
        a.setId_funcionario((Integer) rs.getObject("id_funcionario"));
        a.setId_servicos((Integer) rs.getObject("id_servicos"));
        a.setHorario(rs.getString("horario"));
        a.setDia(rs.getString("dia"));
        a.setHorario_fim(rs.getString("horario_fim"));
        a.setStatus((Integer) rs.getObject("status"));
        return a;
    };

    // Colunas usadas nas leituras (evita repetição)
    private static final String COLUNAS =
        "id_agenda, id_usuario, id_funcionario, id_servicos, horario, dia, horario_fim, status";

    /** Lista agendamentos com filtros opcionais por funcionário e/ou usuário (null = sem filtro). */
    public List<Agendamento> listar(Integer idFuncionario, Integer idUsuario) {
        StringBuilder sql = new StringBuilder("SELECT " + COLUNAS + " FROM tb_agendados WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        if (idFuncionario != null) {
            sql.append(" AND id_funcionario = ?");
            parametros.add(idFuncionario);
        }
        if (idUsuario != null) {
            sql.append(" AND id_usuario = ?");
            parametros.add(idUsuario);
        }
        sql.append(" ORDER BY id_agenda");

        return jdbc.query(sql.toString(), mapper, parametros.toArray());
    }

    /** Busca um agendamento pelo ID. */
    public Optional<Agendamento> buscarPorId(int id) {
        return jdbc.query("SELECT " + COLUNAS + " FROM tb_agendados WHERE id_agenda = ?", mapper, id)
                   .stream().findFirst();
    }

    /** Cria um agendamento e retorna o ID gerado. */
    public int salvar(Agendamento a) {
        return jdbc.queryForObject(
            "INSERT INTO tb_agendados (id_usuario, id_funcionario, id_servicos, horario, dia, horario_fim, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_agenda",
            Integer.class,
            a.getId_usuario(), a.getId_funcionario(), a.getId_servicos(),
            a.getHorario(), a.getDia(), a.getHorario_fim(), a.getStatus());
    }

    /** Atualiza um agendamento existente. */
    public boolean atualizar(int id, Agendamento a) {
        return jdbc.update(
            "UPDATE tb_agendados SET id_usuario = ?, id_funcionario = ?, id_servicos = ?, " +
            "horario = ?, dia = ?, horario_fim = ?, status = ? WHERE id_agenda = ?",
            a.getId_usuario(), a.getId_funcionario(), a.getId_servicos(),
            a.getHorario(), a.getDia(), a.getHorario_fim(), a.getStatus(), id) > 0;
    }

    /** Remove um agendamento pelo ID. */
    public boolean deletar(int id) {
        return jdbc.update("DELETE FROM tb_agendados WHERE id_agenda = ?", id) > 0;
    }
}
