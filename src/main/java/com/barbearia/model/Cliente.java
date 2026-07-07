package com.barbearia.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Modelo da tabela "clientes".
 * Colunas: id_cliente (PK), nome, email, senha (hash BCrypt).
 */
public class Cliente {

    private Integer id_cliente;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    public Cliente() {}

    public Integer getId_cliente() { return id_cliente; }
    public void setId_cliente(Integer id_cliente) { this.id_cliente = id_cliente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
