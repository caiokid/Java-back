package com.barbearia.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.barbearia.model.Funcionario;

/**
 * DAO da tabela "funcionarios" usando JdbcTemplate.
 */
@Repository
public class FuncionarioDAO {

    private final JdbcTemplate jdbc;

    public FuncionarioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Funcionario> mapper = (rs, linha) -> {
        Funcionario f = new Funcionario();
        f.setId_funcionario(rs.getInt("id_funcionario"));
        f.setNome_funcionario(rs.getString("nome_funcionario"));
        f.setEmail(rs.getString("email"));
        f.setImagem(rs.getString("imagem"));
        return f;
    };

    private final RowMapper<Funcionario> mapperId = (rs, linha) -> {
        Funcionario f = new Funcionario();
        f.setId_funcionario(rs.getInt("id_funcionario"));
        return f;
    };


    private final RowMapper<Funcionario> mapperEmpoloys = (rs, linha) -> {
        Funcionario f = new Funcionario();
        f.setId_funcionario(rs.getInt("id_funcionario"));
        f.setNome_funcionario(rs.getString("nome_funcionario"));
        return f;
    };

    /** Lista todos os funcionários (sem a senha). */
    public List<Funcionario> listar() {
        return jdbc.query(
            "SELECT id_funcionario, nome_funcionario, email, imagem FROM funcionarios ORDER BY id_funcionario",
            mapper);
    }
    
    /** Lista todos os funcionários (sem a senha). */
    public List<Funcionario> listarEmploy() {
        return jdbc.query(
            "SELECT id_funcionario, nome_funcionario FROM funcionarios ORDER BY id_funcionario",
            mapperEmpoloys);
    }


    /** Busca um funcionário pelo ID. */
    public Optional<Funcionario> buscarPorId(int id) {
        return jdbc.query(
            "SELECT id_funcionario FROM funcionarios WHERE id_funcionario = ?",
            mapperId, id).stream().findFirst();
    }

    /** Busca pelo email INCLUINDO a senha — usado apenas pelo login. */
    public Optional<Funcionario> buscarPorEmailComSenha(String email) {
        RowMapper<Funcionario> mapperComSenha = (rs, linha) -> {
            Funcionario f = new Funcionario();
            f.setId_funcionario(rs.getInt("id_funcionario"));
            f.setNome_funcionario(rs.getString("nome_funcionario"));
            f.setEmail(rs.getString("email"));
            f.setSenha(rs.getString("senha"));
            f.setImagem(rs.getString("imagem"));
            return f;
        };
        return jdbc.query(
            "SELECT id_funcionario, nome_funcionario, email, senha, imagem FROM funcionarios WHERE email = ?",
            mapperComSenha, email).stream().findFirst();
    }

    /** Insere um funcionário e retorna o ID gerado. */
    public int salvar(Funcionario f) {
        return jdbc.queryForObject(
            "INSERT INTO funcionarios (nome_funcionario, email, senha, imagem) VALUES (?, ?, ?, ?) RETURNING id_funcionario",
            Integer.class, f.getNome_funcionario(), f.getEmail(), f.getSenha(), f.getImagem());
    }

    /** Atualiza os dados sem mexer na senha. */
    public boolean atualizar(int id, Funcionario f) {
        return jdbc.update(
            "UPDATE funcionarios SET nome_funcionario = ?, email = ?, imagem = ? WHERE id_funcionario = ?",
            f.getNome_funcionario(), f.getEmail(), f.getImagem(), id) > 0;
    }

    /** Atualiza incluindo a senha (hash já gerado no service). */
    public boolean atualizarComSenha(int id, Funcionario f) {
        return jdbc.update(
            "UPDATE funcionarios SET nome_funcionario = ?, email = ?, imagem = ?, senha = ? WHERE id_funcionario = ?",
            f.getNome_funcionario(), f.getEmail(), f.getImagem(), f.getSenha(), id) > 0;
    }

    /** Atualiza SOMENTE a coluna imagem (grava o nome do arquivo). */
    public boolean atualizarImagem(int id, String nomeImagem) {
        return jdbc.update(
            "UPDATE funcionarios SET imagem = ? WHERE id_funcionario = ?",
            nomeImagem, id) > 0;
    }

    /** Remove um funcionário pelo ID. */
    public boolean deletar(int id) {
        return jdbc.update("DELETE FROM funcionarios WHERE id_funcionario = ?", id) > 0;
    }
}
