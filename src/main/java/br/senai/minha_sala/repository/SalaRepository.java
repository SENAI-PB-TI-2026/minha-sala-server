package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Sala;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    @Query(value = "SELECT * FROM salas WHERE ativo = TRUE", nativeQuery = true)
    List<Sala> findSalasAtivas();
}
