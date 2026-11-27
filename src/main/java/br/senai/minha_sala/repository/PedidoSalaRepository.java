package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.PedidoSala;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoSalaRepository extends JpaRepository<PedidoSala, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PedidoSala p JOIN FETCH p.aula WHERE p.id = :pedidoId")
    Optional<PedidoSala> findByIdForUpdate(@Param("pedidoId") Long pedidoId);
}
