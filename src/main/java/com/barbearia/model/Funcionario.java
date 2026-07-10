package com.barbearia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Modelo da tabela "funcionarios".
 * Colunas: id_funcionario (PK), nome_funcionario, email, senha, imagem.
 */
public class Funcionario {

    private Integer id_funcionario;

    @NotBlank(message = "O nome do funcionário é obrigatório")
    private String nome_funcionario;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    // Aceita no JSON de entrada, mas nunca aparece nas respostas
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private String imagem; // Caminho/URL da foto do funcionário

    public Funcionario() {}

    public Integer getId_funcionario() { return id_funcionario; }
    public void setId_funcionario(Integer id_funcionario) { this.id_funcionario = id_funcionario; }

    public String getNome_funcionario() { return nome_funcionario; }
    public void setNome_funcionario(String nome_funcionario) { this.nome_funcionario = nome_funcionario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }
}
