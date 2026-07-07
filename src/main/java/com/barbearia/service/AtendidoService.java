package com.barbearia.service;

import com.barbearia.dao.AtendidoDAO;
import com.barbearia.exception.RecursoNaoEncontradoException;
import com.barbearia.model.Atendido;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio do histórico de atendimentos concluídos.
 */
@Service
public class AtendidoService {

    private final AtendidoDAO dao;

    public AtendidoService(AtendidoDAO dao) {
        this.dao = dao;
    }

    public List<Atendido> listar(String funcionario) {
        return dao.listar(funcionario);
    }

    public void deletar(int id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Registro não encontrado");
        }
    }
}
