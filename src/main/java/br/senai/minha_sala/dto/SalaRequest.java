package br.senai.minha_sala.dto;

import br.senai.minha_sala.model.TipoSala;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SalaRequest(
        @NotBlank(message = "O nome da sala deve ser informado")
        @Size(max = 255, message = "O nome da sala deve ter no máximo 255 caracteres")
        String nome,
        @Positive(message = "A capacidade deve ser maior que zero")
        int capacidade,
        @NotNull(message = "O tipo da sala é obrigatório")
        TipoSala tipo,
        @Size(max = 1000, message = "Os recursos devem ter no máximo 1000 caracteres")
        String recursos
) {
}
