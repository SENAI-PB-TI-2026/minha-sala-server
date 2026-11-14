package br.senai.minha_sala.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record AulaRequest(
        @NotNull(message = "A turma deve ser informada")
        @Positive(message = "O ID da turma deve ser positivo")
        Long turmaId,

        @NotBlank(message = "O docente deve ser informado")
        @Size(max = 255, message = "O nome do docente deve ter no máximo 255 caracteres")
        String docente,

        @NotNull(message = "A data deve ser informada")
        LocalDate data,

        @NotNull(message = "O horário de início deve ser informado")
        LocalTime inicio,

        @NotNull(message = "O horário de fim deve ser informado")
        LocalTime fim
) {
    @AssertTrue(message = "O horário de fim deve ser posterior ao horário de início")
    public boolean isFimPosteriorAoInicio() {
        return inicio == null || fim == null || fim.isAfter(inicio);
    }
}
