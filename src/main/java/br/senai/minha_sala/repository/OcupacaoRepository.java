package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Ocupacao;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OcupacaoRepository extends JpaRepository<Ocupacao, Long> {

    @Query("SELECT o FROM Ocupacao o JOIN FETCH o.sala WHERE o.data >= :inicio AND o.data <= :fim")
    List<Ocupacao> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
