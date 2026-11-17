package br.senai.minha_sala.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BloqueioRequest(
        @NotNull(message = "A data de início deve ser informada")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim deve ser informada")
        LocalDate dataFim,

        @NotBlank(message = "O motivo deve ser informado")
        String motivo
) {
    @AssertTrue(message = "A data final deve ser posterior ou igual à data inicial")
    public boolean isDataFimValida() {
        return dataInicio == null || dataFim == null || !dataFim.isBefore(dataInicio);
    }
}
