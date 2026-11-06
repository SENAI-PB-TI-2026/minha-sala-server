package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}
