package br.senai.minha_sala.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AulaResponse(Long id, Long turmaId, String docente, LocalDate data,
                           LocalTime inicio, LocalTime fim) {
}
