package br.senai.minha_sala.dto;

import br.senai.minha_sala.model.TipoSala;

public record SalaResponse(Long id, String nome, int capacidade, TipoSala tipo) {
}
