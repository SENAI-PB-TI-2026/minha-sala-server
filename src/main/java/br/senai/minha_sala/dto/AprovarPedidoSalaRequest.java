package br.senai.minha_sala.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AprovarPedidoSalaRequest(
        @NotNull(message = "A sala deve ser informada")
        @Positive(message = "O ID da sala deve ser positivo")
        Long salaId
) {}
