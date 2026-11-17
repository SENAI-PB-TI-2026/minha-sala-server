package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Aula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByTurmaId(Long turmaId);
}
