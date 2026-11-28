package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.PedidoSala;
import br.senai.minha_sala.model.StatusPedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoSalaRepository extends JpaRepository<PedidoSala, Long> {
    @Query("SELECT p FROM PedidoSala p JOIN FETCH p.aula WHERE p.status = :status ORDER BY p.id")
    List<PedidoSala> findByStatusWithAula(@Param("status") StatusPedido status);
}
