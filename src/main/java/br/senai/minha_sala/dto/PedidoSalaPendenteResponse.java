package br.senai.minha_sala.dto;

import br.senai.minha_sala.model.StatusPedido;

public record PedidoSalaPendenteResponse(
        Long id,
        AulaResponse aula,
        String solicitante,
        String necessidade,
        StatusPedido status
) {}
