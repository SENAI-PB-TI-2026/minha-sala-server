package br.senai.minha_sala.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PedidoSalaRequest(
        @NotNull(message = "A aula deve ser informada")
        @Positive(message = "O ID da aula deve ser positivo")
        Long aulaId,

        @NotBlank(message = "O solicitante deve ser informado")
        @Size(max = 255, message = "O solicitante deve ter no máximo 255 caracteres")
        String solicitante,

        @NotBlank(message = "A necessidade de sala deve ser informada")
        @Size(max = 1000, message = "A necessidade deve ter no máximo 1000 caracteres")
        String necessidade
) {
    public PedidoSalaRequest {
        if (solicitante != null) solicitante = solicitante.strip();
        if (necessidade != null) necessidade = necessidade.strip();
    }
}
