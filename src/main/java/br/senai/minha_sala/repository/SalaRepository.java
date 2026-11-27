package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Sala;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Sala s WHERE s.id = :salaId")
    Optional<Sala> findByIdForUpdate(@Param("salaId") Long salaId);

    List<Sala> findByAtivoTrue();
}
