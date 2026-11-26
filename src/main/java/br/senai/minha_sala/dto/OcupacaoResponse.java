package br.senai.minha_sala.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record OcupacaoResponse(
        Long id,
        Long salaId,
        String salaNome,
        Long aulaId,
        LocalDate data,
        LocalTime inicio,
        LocalTime fim,
        String descricao
) {
}
