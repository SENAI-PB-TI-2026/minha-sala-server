package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
}
