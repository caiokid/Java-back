package com.barbearia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barbearia.dao.AgendamentoDAO;
import com.barbearia.dao.FuncionarioDAO;
import com.barbearia.dao.ServicoDAO;
import com.barbearia.dto.request.AgendamentoRequest;
import com.barbearia.dto.response.AgendamentoResponse;
import com.barbearia.exception.RecursoNaoEncontradoException;
import com.barbearia.model.Agendamento;
import com.barbearia.model.Funcionario;
import com.barbearia.model.Servico;






/**
 * Regras de negócio dos agendamentos (tabela tb_agendados).
 */
@Service
public class AgendamentoService {

    
    private final FuncionarioDAO funcionarioDAO;
    private final ServicoDAO servicoDAO;
    private final AgendamentoDAO agendamentoDAO;


    public AgendamentoService(FuncionarioDAO funcionarioDAO, ServicoDAO servicoDAO, AgendamentoDAO agendamentoDAO) {
        this.funcionarioDAO = funcionarioDAO;
        this.servicoDAO = servicoDAO;
        this.agendamentoDAO = agendamentoDAO;
    }

    public List<Agendamento> listar(Integer idFuncionario, Integer idUsuario) {
        return agendamentoDAO.listar(idFuncionario, idUsuario);
    }


    public Agendamento buscarPorId(int id) {
        return agendamentoDAO.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));
    }

  public AgendamentoResponse salvar(AgendamentoRequest req, Integer userId) {

   System.out.println("=== INICIOU salvar ===");
    System.out.println("userId: " + userId);
    System.out.println("req completo: " + req);   // mostra todos os campos do record
    System.out.println("id_funcionario: " + req.id_funcionario());
    System.out.println("id_servicos: " + req.id_servicos());
    System.out.println("horario: " + req.horario());
    System.out.println("dia: " + req.dia());
    System.out.println("duration: " + req.duration());



    // 1. Valida funcionário
    Funcionario func = funcionarioDAO.buscarPorId(Integer.parseInt(req.id_funcionario()))
            .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado"));
            
    // 2. Valida serviço
    Servico servico = servicoDAO.buscarPorid(Integer.parseInt(req.id_servicos()))
            .orElseThrow(() -> new RecursoNaoEncontradoException("Serviço não encontrado"));

    // 3. Calcula o horário fim
    // String horarioFim = calcularHorarioFimgit push -u origin main(req.horario(), servico.getDuracao());



    // 5. Salva em TB_AGENDADOS (o agendamento do cliente)
    Agendamento ag = new Agendamento();
    ag.setId_usuario(userId);
    ag.setId_funcionario(func.getId_funcionario());
    ag.setId_servicos(servico.getId_servicos());
    ag.setHorario(req.horario());
    ag.setDia(req.dia());
    ag.setHorario_fim(req.duration());
    ag.setStatus(1);
    agendamentoDAO.salvar(ag);

    // 6. Resposta
    return new AgendamentoResponse(
            "Appointment scheduled successfully!",
            req.horario(),
            req.dia(),
            servico.getServico(),
            servico.getDuracao());
}

    /** Soma a duração (minutos) ao horário início. Ex: "14:00" + "30" → "14:30". */
    private String calcularHorarioFim(String inicio, String duracao) {
        String[] partes = inicio.split(":");
        int horaInicio = Integer.parseInt(partes[0]);
        int minInicio = Integer.parseInt(partes[1]);
        int duracaoMin = Integer.parseInt(duracao.replaceAll("[^0-9]", ""));

        int totalMin = horaInicio * 60 + minInicio + duracaoMin;
        return String.format("%02d:%02d", totalMin / 60, totalMin % 60);
    }
}
