package com.barbearia.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.barbearia.dao.ClienteDAO;
import com.barbearia.dto.request.LoginRequest;
import com.barbearia.dto.response.LoginResponse;
import com.barbearia.exception.CredenciaisInvalidasException;
import com.barbearia.model.Cliente;
import com.barbearia.security.JwtService;

/**
 * Serviço de autenticação: valida email + senha e devolve um token JWT.
 */
@Service
public class AuthService {

    private final ClienteDAO clienteDAO;
    private final JwtService jwtService;

    public AuthService(ClienteDAO clienteDAO, JwtService jwtService) {
        this.clienteDAO = clienteDAO;
        this.jwtService = jwtService;
    }

    /**
     * Realiza o login. O campo "tipo" define onde buscar:
     * "funcionario" → tabela funcionarios; ausente ou "cliente" → tabela clientes.
     * Em caso de sucesso, gera um token JWT e o devolve no LoginResponse.
     */
    public LoginResponse login(LoginRequest req) {
        // Padrão: login de cliente
        Cliente c = clienteDAO.buscarPorEmailComSenha(req.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("Email ou senha inválidos"));
        if (!senhaConfere(req.senha(), c.getSenha())) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos");
        }
        String token = jwtService.gerarToken(
                c.getEmail(), "cliente", c.getId_cliente(), c.getNome());
        return new LoginResponse(token, "cliente",
                c.getId_cliente(), c.getNome(), c.getEmail(),
                "Login realizado com sucesso!");
    }

 
    private boolean senhaConfere(String digitada, String armazenada) {
        if (armazenada == null) return false;
        if (armazenada.startsWith("$2a$") || armazenada.startsWith("$2b$") || armazenada.startsWith("$2y$")) {
            return BCrypt.checkpw(digitada, armazenada);
        }
        return armazenada.equals(digitada);
    }
}