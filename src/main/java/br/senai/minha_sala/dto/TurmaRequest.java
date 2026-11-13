package br.senai.minha_sala.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TurmaRequest(
        @NotBlank(message = "O nome da turma deve ser informado")
        @Size(max = 255, message = "O nome da turma deve ter no máximo 255 caracteres")
        String nome,

        @NotBlank(message = "O curso deve ser informado")
        @Size(max = 255, message = "O curso deve ter no máximo 255 caracteres")
        String curso,

        @Positive(message = "A quantidade de alunos deve ser maior que zero")
        int quantidadeAlunos
) {
}
