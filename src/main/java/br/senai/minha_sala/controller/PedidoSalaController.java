package br.senai.minha_sala.controller;

import br.senai.minha_sala.dto.PedidoSalaRequest;
import br.senai.minha_sala.dto.AprovarPedidoSalaRequest;
import br.senai.minha_sala.dto.PedidoSalaPendenteResponse;
import br.senai.minha_sala.dto.PedidoSalaResponse;
import br.senai.minha_sala.service.PedidoSalaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/pedidos-sala")
public class PedidoSalaController {
    private final PedidoSalaService pedidoSalaService;

    public PedidoSalaController(PedidoSalaService pedidoSalaService) {
        this.pedidoSalaService = pedidoSalaService;
    }

    @GetMapping
    public List<PedidoSalaResponse> listar() {
        return pedidoSalaService.listar();
    }

    @GetMapping("/pendentes")
    public List<PedidoSalaPendenteResponse> listarPendentes() {
        return pedidoSalaService.listarPendentes();
    }

    @GetMapping("/{id}")
    public PedidoSalaResponse buscarPorId(@PathVariable Long id) {
        return pedidoSalaService.buscarPorId(id);
    }

    @PostMapping("/{id}/aprovar")
    public PedidoSalaResponse aprovar(@PathVariable Long id,
                                      @Valid @RequestBody AprovarPedidoSalaRequest dados) {
        return pedidoSalaService.aprovar(id, dados.salaId());
    }

    @PostMapping
    public ResponseEntity<PedidoSalaResponse> criar(@Valid @RequestBody PedidoSalaRequest dados) {
        PedidoSalaResponse criado = pedidoSalaService.criar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }
}
