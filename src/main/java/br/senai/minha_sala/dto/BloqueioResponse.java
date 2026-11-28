package br.senai.minha_sala.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueioResponse(
        Long id,
        Long salaId,
        String salaNome,
        LocalDate dataInicio,
        LocalDate dataFim,
        LocalTime inicio,
        LocalTime fim,
        String motivo
) {
}
