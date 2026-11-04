package br.senai.minha_sala.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sala {
    private Long id;
    private String nome;
    private int capacidade;

}
