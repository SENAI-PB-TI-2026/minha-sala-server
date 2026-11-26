package br.senai.minha_sala.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record OcupacaoRequest(
        @NotNull(message = "A data deve ser informada")
        LocalDate data,

        @NotNull(message = "O horário de início deve ser informado")
        LocalTime inicio,

        @NotNull(message = "O horário de fim deve ser informado")
        LocalTime fim,

        @NotBlank(message = "A descrição deve ser informada")
        @Size(max = 519, message = "A descrição deve ter no máximo 519 caracteres")
        String descricao
) {
    @AssertTrue(message = "O horário de fim deve ser posterior ao horário de início")
    public boolean isHorarioValido() {
        return inicio == null || fim == null || fim.isAfter(inicio);
    }
}
