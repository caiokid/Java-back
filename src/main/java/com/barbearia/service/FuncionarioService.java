package com.barbearia.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.barbearia.dao.FuncionarioDAO;
import com.barbearia.dto.response.FuncionarioResponse;
import com.barbearia.exception.EmailJaCadastradoException;
import com.barbearia.exception.RecursoNaoEncontradoException;
import com.barbearia.model.Funcionario;
import com.barbearia.util.ImagemUrlHelper;

/**
 * Regras de negócio de funcionários: email único, senha com hash BCrypt
 * e upload/exibição da foto (seção "Nossa Equipe").
 */
@Service
public class FuncionarioService {

    private final FuncionarioDAO dao;
    private final ArmazenamentoImagemService armazenamento;

    public FuncionarioService(FuncionarioDAO dao, ArmazenamentoImagemService armazenamento) {
        this.dao = dao;
        this.armazenamento = armazenamento;
    }

    /** Lista os funcionários já como response (com a URL da imagem montada). */
    public List<FuncionarioResponse> listar() {
        String baseUrl = baseUrlAtual();
        return dao.listar().stream()
                .map(f -> toResponse(f, baseUrl))
                .toList();
    }


    public List<Funcionario> listarEmploys() {
        return dao.listarEmploy();
    } 

    /** Busca um funcionário como response (com a URL da imagem montada). */
    public FuncionarioResponse buscarPorId(int id) {
        Funcionario f = dao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado"));
        return toResponse(f, baseUrlAtual());
    }

    public Funcionario salvar(Funcionario func) {
        if (func.getSenha() == null || func.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória");
        }
        if (dao.buscarPorEmailComSenha(func.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        func.setSenha(BCrypt.hashpw(func.getSenha(), BCrypt.gensalt()));
        func.setId_funcionario(dao.salvar(func));
        return func;
    }

    public Funcionario atualizar(int id, Funcionario func) {
        boolean atualizado;
        if (func.getSenha() != null && !func.getSenha().isBlank()) {
            func.setSenha(BCrypt.hashpw(func.getSenha(), BCrypt.gensalt()));
            atualizado = dao.atualizarComSenha(id, func);
        } else {
            atualizado = dao.atualizar(id, func);
        }
        if (!atualizado) throw new RecursoNaoEncontradoException("Funcionário não encontrado");

        func.setId_funcionario(id);
        return func;
    }

    /**
     * Recebe o arquivo, salva no disco (via ArmazenamentoImagemService),
     * grava o NOME na coluna imagem e devolve o funcionário já com a URL.
     */
    public FuncionarioResponse atualizarImagem(int id, MultipartFile arquivo) {
        // Garante que o funcionário existe antes de salvar o arquivo
        Funcionario f = dao.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado"));

        String nomeArquivo = armazenamento.salvar(arquivo); // grava no disco, retorna o nome
        dao.atualizarImagem(id, nomeArquivo);               // persiste só o nome no banco

        f.setImagem(nomeArquivo);
        return toResponse(f, baseUrlAtual());
    }

    public void deletar(int id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Funcionário não encontrado");
        }
    }

    /** Converte o model em response, montando a URL pública da imagem. */
    private FuncionarioResponse toResponse(Funcionario f, String baseUrl) {
        // Só monta a URL se o arquivo existir de fato na pasta images/.
        // Assim valores inválidos no banco (ex: "123343") viram null em vez de URL quebrada.
        String imagem = armazenamento.existe(f.getImagem()) ? f.getImagem() : null;
        return new FuncionarioResponse(
                f.getNome_funcionario(),
                f.getEmail(),
                ImagemUrlHelper.buildImageUrl(baseUrl, imagem));
    }

    /** Base URL da requisição atual (ex: http://localhost:8080). */
    private String baseUrlAtual() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
}
