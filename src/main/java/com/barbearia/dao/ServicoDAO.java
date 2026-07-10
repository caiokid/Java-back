package com.barbearia.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.barbearia.model.Servico;

/**
 * DAO da tabela "servicos" usando JdbcTemplate.
 */
@Repository
public class ServicoDAO {

    private final JdbcTemplate jdbc;

    public ServicoDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Servico> mapper = (rs, linha) -> {
        Servico s = new Servico();
        s.setId_servicos(rs.getInt("id_servicos"));
        s.setServico(rs.getString("servico"));
        s.setPreco(rs.getString("preco"));
        s.setDesc(rs.getString("descricao"));
        s.setDuracao(rs.getString("descricao"));
        return s;
    };

    private final RowMapper<Servico> mapperOne = (rs, linha) -> {
        Servico s = new Servico();
        s.setId_servicos(rs.getInt("id_servicos"));
        return s;
    };

    private final RowMapper<Servico> mapperId = (rs, linha) -> {
        Servico s = new Servico();
        s.setId_servicos(rs.getInt("id_servicos"));
        s.setServico(rs.getString("servico"));
        s.setPreco(rs.getString("preco"));
        s.setDesc(rs.getString("descricao"));
        s.setDuracao(rs.getString("duracao"));
        return s;
    };


       /** Lista todos os serviços. */
    public List<Servico> listar() {
        return jdbc.query("SELECT id_servicos, servico, preco, descricao, duracao FROM servicos ORDER BY id_servicos", mapper);
    }

    /** Lista todos os serviços. */
    public List<Servico> listarId(Integer id) {
        return jdbc.query("SELECT id_servicos, servico, preco, descricao, duracao FROM servicos WHERE id_servicos = ?", mapperId, id);
    }

    /** Busca um serviço pelo ID. */
    public Optional<Servico> buscarPorId(int id) {
        return jdbc.query("SELECT id_servicos, servico, preco FROM servicos WHERE id_servicos = ?", mapper, id)
                   .stream().findFirst();

    }

     /** Busca um serviço pelo ID. */
    public Optional<Servico> buscarPorid(int id) {
        return jdbc.query("SELECT id_servicos FROM servicos WHERE id_servicos = ?", mapperOne, id)
                   .stream().findFirst();

    }

    /** Insere um serviço e retorna o ID gerado. */
    public int salvar(Servico s) {
        return jdbc.queryForObject(
            "INSERT INTO servicos (servico, preco) VALUES (?, ?) RETURNING id_servicos",
            Integer.class, s.getServico(), s.getPreco());
    }

    /** Atualiza um serviço. */
    public boolean atualizar(int id, Servico s) {
        return jdbc.update("UPDATE servicos SET servico = ?, preco = ? WHERE id_servicos = ?",
                           s.getServico(), s.getPreco(), id) > 0;
    }

    /** Remove um serviço pelo ID. */
    public boolean deletar(int id) {
        return jdbc.update("DELETE FROM servicos WHERE id_servicos = ?", id) > 0;
    }
}
