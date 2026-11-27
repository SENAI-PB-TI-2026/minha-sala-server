package br.senai.minha_sala.dto;

import br.senai.minha_sala.model.StatusPedido;

public record PedidoSalaResponse(
        Long id,
        Long aulaId,
        String solicitante,
        String necessidade,
        StatusPedido status
) {}
