package com.barbearia.model;

public class Comentarios {

    private Integer id_comentario;
    private String texto;
    private String autor;
    private Integer id_usuario;

    public Comentarios() {}

    public Integer getId_comentario() { return id_comentario; }
    public void setId_comentario(Integer id_comentario) {
        this.id_comentario = id_comentario;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAutor() { return autor; }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
}