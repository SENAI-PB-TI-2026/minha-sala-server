package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Aula;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByTurmaId(Long turmaId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Aula a WHERE a.id = :aulaId")
    Optional<Aula> findByIdForUpdate(@Param("aulaId") Long aulaId);
}
