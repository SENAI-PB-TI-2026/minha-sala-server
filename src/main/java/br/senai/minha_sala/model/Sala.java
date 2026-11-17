package br.senai.minha_sala.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "salas")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "O nome da sala deve ser informado")
    @Size(max = 255, message = "O nome da sala deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String nome;
    @Positive(message = "A capacidade deve ser maior que zero")
    private int capacidade;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSala tipo;

}
