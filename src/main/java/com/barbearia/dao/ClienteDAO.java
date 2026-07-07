package com.barbearia.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.barbearia.model.Cliente;

/**
 * DAO da tabela "clientes" usando JdbcTemplate do Spring
 * (gerencia conexões, statements e exceções automaticamente).
 */
@Repository
public class ClienteDAO {

    private final JdbcTemplate jdbc;

    public ClienteDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Converte cada linha do ResultSet em um objeto Cliente (sem a senha). */
    private final RowMapper<Cliente> mapper = (rs, linha) -> {
        Cliente c = new Cliente();
        c.setId_cliente(rs.getInt("id_cliente"));
        c.setNome(rs.getString("nome"));
        c.setEmail(rs.getString("email"));
        return c;
    };

    /** Lista todos os clientes (sem a senha). */
    public List<Cliente> listar() {
        return jdbc.query("SELECT id_cliente, nome, email FROM clientes ORDER BY id_cliente", mapper);
    }

    /** Busca um cliente pelo ID. Optional vazio se não existir. */
    public Optional<Cliente> buscarPorId(int id) {
        return jdbc.query("SELECT id_cliente, nome, email FROM clientes WHERE id_cliente = ?", mapper, id)
                   .stream().findFirst();
    }

    /** Busca pelo email INCLUINDO a senha (hash) — usado apenas pelo login. */
    public Optional<Cliente> buscarPorEmailComSenha(String email) {
        RowMapper<Cliente> mapperComSenha = (rs, linha) -> {
            Cliente c = new Cliente();
            c.setId_cliente(rs.getInt("id_cliente"));
            c.setNome(rs.getString("nome"));
            c.setEmail(rs.getString("email"));
            c.setSenha(rs.getString("senha"));
            return c;
        };
        return jdbc.query("SELECT id_cliente, nome, email, senha FROM clientes WHERE email = ?",
                          mapperComSenha, email)
                   .stream().findFirst();
                   
    }

    /** Insere um cliente (senha já deve vir com hash do service) e retorna o ID gerado. */
    public int salvar(Cliente c) {
        return jdbc.queryForObject(
                "INSERT INTO clientes (nome, email, senha) VALUES (?, ?, ?) RETURNING id_cliente",
                Integer.class, c.getNome(), c.getEmail(), c.getSenha());
    }

    /** Atualiza nome/email. Retorna true se alguma linha foi alterada. */
    public boolean atualizar(int id, Cliente c) {
        return jdbc.update("UPDATE clientes SET nome = ?, email = ? WHERE id_cliente = ?",
                           c.getNome(), c.getEmail(), id) > 0;
    }

    /** Atualiza também a senha (hash já gerado no service). */
    public boolean atualizarComSenha(int id, Cliente c) {
        return jdbc.update("UPDATE clientes SET nome = ?, email = ?, senha = ? WHERE id_cliente = ?",
                           c.getNome(), c.getEmail(), c.getSenha(), id) > 0;
    }

    /** Remove um cliente pelo ID. */
    public boolean deletar(int id) {
        return jdbc.update("DELETE FROM clientes WHERE id_cliente = ?", id) > 0;
    }
}
