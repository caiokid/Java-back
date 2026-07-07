package com.barbearia.dao;

import com.barbearia.model.Atendido;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO da tabela "atendidos" (histórico de atendimentos concluídos) usando JdbcTemplate.
 */
@Repository
public class AtendidoDAO {

    private final JdbcTemplate jdbc;

    public AtendidoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Atendido> mapper = (rs, linha) -> {
        Atendido a = new Atendido();
        a.setId_atendidos(rs.getInt("id_atendidos"));
        a.setId_agenda(rs.getInt("id_agenda"));
        a.setUsuario(rs.getString("usuario"));
        a.setFuncionario(rs.getString("funcionario"));
        a.setServicos(rs.getString("servicos"));
        a.setHorario(rs.getString("horario"));
        return a;
    };

    /** Lista os atendimentos concluídos; filtro opcional por funcionário. */
    public List<Atendido> listar(String funcionario) {
        String sql = "SELECT id_atendidos, id_agenda, usuario, funcionario, servicos, horario FROM atendidos";
        if (funcionario != null) {
            return jdbc.query(sql + " WHERE funcionario = ? ORDER BY id_atendidos DESC", mapper, funcionario);
        }
        return jdbc.query(sql + " ORDER BY id_atendidos DESC", mapper);
    }

    /** Remove um registro do histórico. */
    public boolean deletar(int id) {
        return jdbc.update("DELETE FROM atendidos WHERE id_atendidos = ?", id) > 0;
    }
}
