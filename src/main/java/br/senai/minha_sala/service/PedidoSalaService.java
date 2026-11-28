package br.senai.minha_sala.service;

import br.senai.minha_sala.dto.PedidoSalaRequest;
import br.senai.minha_sala.dto.PedidoSalaResponse;
import br.senai.minha_sala.dto.PedidoSalaPendenteResponse;
import br.senai.minha_sala.dto.AulaResponse;
import br.senai.minha_sala.model.PedidoSala;
import br.senai.minha_sala.model.StatusPedido;
import br.senai.minha_sala.repository.AulaRepository;
import br.senai.minha_sala.repository.PedidoSalaRepository;
import br.senai.minha_sala.service.SalaService;
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
    private final SalaService salaService;

    public PedidoSalaService(PedidoSalaRepository pedidoSalaRepository, AulaRepository aulaRepository,
                             SalaService salaService) {
        this.pedidoSalaRepository = pedidoSalaRepository;
        this.aulaRepository = aulaRepository;
        this.salaService = salaService;
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

    @Transactional
    public PedidoSalaResponse aprovar(Long pedidoId, Long salaId) {
        PedidoSala pedido = pedidoSalaRepository.findByIdForUpdate(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        if (pedido.getStatus() == StatusPedido.REJEITADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Um pedido rejeitado não pode ser aprovado");
        }

        salaService.registrarAulaEmSala(salaId, pedido.getAula().getId());
        pedido.setStatus(StatusPedido.APROVADO);
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
