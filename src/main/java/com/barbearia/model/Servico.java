package com.barbearia.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Modelo da tabela "servicos".
 * Colunas: id_servicos (PK), servico, preco (varchar(50) no banco — mantido como String).
 */
public class Servico {

    private Integer id_servicos;

    @NotBlank(message = "O nome do serviço é obrigatório")
    private String servico;

    @NotBlank(message = "O preço é obrigatório")
    private String preco; // String para corresponder ao varchar(50) do banco

    @NotBlank(message= "O preço é obrigatório")   
    private String descricao;
    
    @NotBlank(message= "A duração é obrigatório")      
    private String duracao;

    public Servico() {}

    public Integer getId_servicos() { return id_servicos; }
    public void setId_servicos(Integer id_servicos) { this.id_servicos = id_servicos; }

    public String getServico() { return servico; }
    public void setServico(String servico) { this.servico = servico; }

    public String getPreco() { return preco; }
    public void setPreco(String preco) { this.preco = preco; }

    public String getDesc(){ return descricao;}
    public void setDesc(String descricao ) { this.descricao = descricao; }

    public String getDuracao(){ return duracao;}
    public void setDuracao(String duracao) { this.duracao = duracao;}
}
