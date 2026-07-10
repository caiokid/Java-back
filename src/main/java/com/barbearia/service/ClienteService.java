package com.barbearia.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.barbearia.dao.ClienteDAO;
import com.barbearia.exception.EmailJaCadastradoException;
import com.barbearia.exception.RecursoNaoEncontradoException;
import com.barbearia.model.Cliente;

/**
 * Regras de negócio de clientes: validação de email duplicado
 * e criptografia da senha com BCrypt antes de gravar.
 */
@Service
public class ClienteService {

    private final ClienteDAO dao;

    public ClienteService(ClienteDAO dao) {
        this.dao = dao;
    }

    public List<Cliente> listar() {
        return dao.listar();
    }

    public Cliente buscarPorId(int id) {
        return dao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    public Cliente salvar(Cliente cliente) {
        // Senha é obrigatória apenas no cadastro
        if (cliente.getSenha() == null || cliente.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória");
        }

        // Bloqueia email duplicado
        if (dao.buscarPorEmailComSenha(cliente.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }
        

        // Grava o hash BCrypt, nunca a senha em texto puro
        cliente.setSenha(BCrypt.hashpw(cliente.getSenha(), BCrypt.gensalt()));
        cliente.setId_cliente(dao.salvar(cliente));
        return cliente;
    }

    public Cliente atualizar(int id, Cliente cliente) {
        boolean atualizado;
        if (cliente.getSenha() != null && !cliente.getSenha().isBlank()) {
            // Veio senha nova → atualiza com o novo hash
            cliente.setSenha(BCrypt.hashpw(cliente.getSenha(), BCrypt.gensalt()));
            atualizado = dao.atualizarComSenha(id, cliente);
        } else {
            atualizado = dao.atualizar(id, cliente);
        }
        if (!atualizado) throw new RecursoNaoEncontradoException("Cliente não encontrado");

        cliente.setId_cliente(id);
        return cliente;
    }

    public void deletar(int id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado");
        }
    }
}
