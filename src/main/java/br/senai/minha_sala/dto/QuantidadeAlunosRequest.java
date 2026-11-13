package br.senai.minha_sala.dto;

import jakarta.validation.constraints.Positive;

public record QuantidadeAlunosRequest(
        @Positive(message = "A quantidade de alunos deve ser maior que zero")
        int quantidadeAlunos
) {
}
