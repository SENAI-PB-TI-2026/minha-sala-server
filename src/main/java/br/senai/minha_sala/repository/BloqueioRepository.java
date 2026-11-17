package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Bloqueio;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BloqueioRepository extends JpaRepository<Bloqueio, Long> {

    @Query("SELECT b FROM Bloqueio b JOIN FETCH b.sala WHERE b.dataInicio <= :fim AND b.dataFim >= :inicio")
    List<Bloqueio> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
