package br.senai.minha_sala.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "turmas")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da turma deve ser informado")
    @Size(max = 255, message = "O nome da turma deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O curso deve ser informado")
    @Size(max = 255, message = "O curso deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String curso;

    @Positive(message = "A quantidade de alunos deve ser maior que zero")
    @Column(name = "quantidade_alunos", nullable = false)
    private int quantidadeAlunos;
}
