package com.barbearia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barbearia.dao.ServicoDAO;
import com.barbearia.exception.RecursoNaoEncontradoException;
import com.barbearia.model.Servico;

/**
 * Regras de negócio dos serviços oferecidos pela barbearia.
 */
@Service
public class ServicoService {

    private final ServicoDAO dao;

    public ServicoService(ServicoDAO dao) {
        this.dao = dao;
    }

    public List<Servico> listar() {
        return dao.listar();
    }

    public List<Servico> listarId(Integer id) {
        return dao.listarId(id);
    }

    public Servico buscarPorId(int id) {
        return dao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));
    }

    public Servico salvar(Servico servico) {
        servico.setId_servicos(dao.salvar(servico));
        return servico;
    }

    public Servico atualizar(int id, Servico servico) {
        if (!dao.atualizar(id, servico)) {
            throw new RecursoNaoEncontradoException("Serviço não encontrado");
        }
        servico.setId_servicos(id);
        return servico;
    }

    public void deletar(int id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Serviço não encontrado");
        }
    }
}
