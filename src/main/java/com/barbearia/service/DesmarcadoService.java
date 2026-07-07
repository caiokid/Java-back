package com.barbearia.service;

import com.barbearia.dao.DesmarcadoDAO;
import com.barbearia.exception.RecursoNaoEncontradoException;
import com.barbearia.model.Desmarcado;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio do histórico de cancelamentos.
 */
@Service
public class DesmarcadoService {

    private final DesmarcadoDAO dao;

    public DesmarcadoService(DesmarcadoDAO dao) {
        this.dao = dao;
    }

    public List<Desmarcado> listar(String funcionario) {
        return dao.listar(funcionario);
    }

    public void marcarComoVisto(int id) {
        if (!dao.marcarComoVisto(id)) {
            throw new RecursoNaoEncontradoException("Registro não encontrado");
        }
    }

    public void deletar(int id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Registro não encontrado");
        }
    }
}
