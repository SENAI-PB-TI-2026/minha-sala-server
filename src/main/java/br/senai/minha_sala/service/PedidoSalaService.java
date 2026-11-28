package br.senai.minha_sala.service;

import br.senai.minha_sala.dto.PedidoSalaRequest;
import br.senai.minha_sala.dto.PedidoSalaResponse;
import br.senai.minha_sala.dto.PedidoSalaPendenteResponse;
import br.senai.minha_sala.dto.AulaResponse;
import br.senai.minha_sala.model.PedidoSala;
import br.senai.minha_sala.model.StatusPedido;
import br.senai.minha_sala.repository.AulaRepository;
import br.senai.minha_sala.repository.PedidoSalaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class PedidoSalaService {
    private final PedidoSalaRepository pedidoSalaRepository;
    private final AulaRepository aulaRepository;

    public PedidoSalaService(PedidoSalaRepository pedidoSalaRepository, AulaRepository aulaRepository) {
        this.pedidoSalaRepository = pedidoSalaRepository;
        this.aulaRepository = aulaRepository;
    }

    public List<PedidoSalaResponse> listar() {
        return pedidoSalaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<PedidoSalaPendenteResponse> listarPendentes() {
        return pedidoSalaRepository.findByStatusWithAula(StatusPedido.PENDENTE).stream()
                .map(this::toPendenteResponse)
                .toList();
    }

    public PedidoSalaResponse buscarPorId(Long id) {
        return toResponse(pedidoSalaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado")));
    }

    @Transactional
    public PedidoSalaResponse criar(PedidoSalaRequest dados) {
        var aula = aulaRepository.findById(dados.aulaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aula não encontrada"));
        PedidoSala pedido = new PedidoSala(null, aula, dados.solicitante(), dados.necessidade(), StatusPedido.PENDENTE);
        return toResponse(pedidoSalaRepository.save(pedido));
    }

    private PedidoSalaResponse toResponse(PedidoSala pedido) {
        return new PedidoSalaResponse(pedido.getId(), pedido.getAula().getId(), pedido.getSolicitante(),
                pedido.getNecessidade(), pedido.getStatus());
    }

    private PedidoSalaPendenteResponse toPendenteResponse(PedidoSala pedido) {
        var aula = pedido.getAula();
        AulaResponse aulaResponse = new AulaResponse(aula.getId(), aula.getTurma().getId(), aula.getDocente(),
                aula.getData(), aula.getInicio(), aula.getFim(), aula.getRecursoObrigatorio());
        return new PedidoSalaPendenteResponse(pedido.getId(), aulaResponse, pedido.getSolicitante(),
                pedido.getNecessidade(), pedido.getStatus());
    }
}
