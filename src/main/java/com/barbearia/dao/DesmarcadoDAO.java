package com.barbearia.dao;

import com.barbearia.model.Desmarcado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO da tabela "desmarcados" (histórico de cancelamentos) usando JdbcTemplate.
 */
@Repository
public class DesmarcadoDAO {

    private final JdbcTemplate jdbc;

    public DesmarcadoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Desmarcado> mapper = (rs, linha) -> {
        Desmarcado d = new Desmarcado();
        d.setId_desmarcados(rs.getInt("id_desmarcados"));
        d.setId_agenda(rs.getInt("id_agenda"));
        d.setUsuario(rs.getString("usuario"));
        d.setFuncionario(rs.getString("funcionario"));
        d.setServicos(rs.getString("servicos"));
        d.setHorario(rs.getString("horario"));
        d.setStatus(rs.getInt("status"));
        d.setViews(rs.getInt("views"));
        return d;
    };

    /** Lista os cancelamentos; filtro opcional por funcionário. */
    public List<Desmarcado> listar(String funcionario) {
        String sql = "SELECT id_desmarcados, id_agenda, usuario, funcionario, servicos, horario, status, views FROM desmarcados";
        if (funcionario != null) {
            return jdbc.query(sql + " WHERE funcionario = ? ORDER BY id_desmarcados DESC", mapper, funcionario);
        }
        return jdbc.query(sql + " ORDER BY id_desmarcados DESC", mapper);
    }

    /** Marca um cancelamento como visualizado (views = 1). */
    public boolean marcarComoVisto(int id) {
        return jdbc.update("UPDATE desmarcados SET views = 1 WHERE id_desmarcados = ?", id) > 0;
    }

    /** Remove um registro de cancelamento. */
    public boolean deletar(int id) {
        return jdbc.update("DELETE FROM desmarcados WHERE id_desmarcados = ?", id) > 0;
    }
}
