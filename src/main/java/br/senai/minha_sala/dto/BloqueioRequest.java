package br.senai.minha_sala.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

public record BloqueioRequest(
        @NotNull(message = "A data deve ser informada")
        LocalDate data,

        @NotNull(message = "O horário de início deve ser informado")
        LocalTime inicio,

        @NotNull(message = "O horário de fim deve ser informado")
        LocalTime fim,

        @NotBlank(message = "O motivo deve ser informado")
        @Pattern(regexp = "SESI|EJA", message = "O motivo deve ser SESI ou EJA")
        String motivo
) {
    @AssertTrue(message = "O horário de fim deve ser posterior ao início")
    public boolean isHorarioValido() {
        return inicio == null || fim == null || fim.isAfter(inicio);
    }
}
