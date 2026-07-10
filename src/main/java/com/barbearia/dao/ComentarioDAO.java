package com.barbearia.dao;


import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.barbearia.model.Comentarios;



@Repository
public class ComentarioDAO{

    private final JdbcTemplate jdbc;

    // Injeção de dependência via construtor — o Spring fornece o JdbcTemplate
    public ComentarioDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Comentarios> mapper = (rs, linha) -> {
        Comentarios comentarios = new Comentarios();
        comentarios.setId_comentario(rs.getInt("id_comentario"));
        comentarios.setTexto(rs.getString("texto"));
        comentarios.setAutor(rs.getString("autor"));




  
        return comentarios;
    };


    public List<Comentarios> listComments() {
        return jdbc.query("SELECT id_comentario, texto, autor, id_usuario FROM comentarios", mapper);
    }
}