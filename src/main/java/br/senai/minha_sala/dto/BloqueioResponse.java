package br.senai.minha_sala.dto;

import java.time.LocalDate;

public record BloqueioResponse(
        Long id,
        Long salaId,
        String salaNome,
        LocalDate dataInicio,
        LocalDate dataFim,
        String motivo
) {
}
