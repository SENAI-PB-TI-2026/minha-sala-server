package br.senai.minha_sala.repository;

import br.senai.minha_sala.model.Ocupacao;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OcupacaoRepository extends JpaRepository<Ocupacao, Long> {

    @Query("SELECT o FROM Ocupacao o JOIN FETCH o.sala LEFT JOIN FETCH o.aula WHERE o.data >= :inicio AND o.data <= :fim")
    List<Ocupacao> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    Optional<Ocupacao> findByAulaId(Long aulaId);

    @Query("""
            SELECT DISTINCT o.sala.id FROM Ocupacao o
            WHERE o.data = :data AND o.inicio < :fim AND o.fim > :inicio
              AND (:salaId IS NULL OR o.sala.id = :salaId)
              AND (:ignorarOcupacaoId IS NULL OR o.id <> :ignorarOcupacaoId)
            """)
    List<Long> buscarIdsSalasOcupadas(@Param("data") LocalDate data, @Param("inicio") LocalTime inicio,
                                      @Param("fim") LocalTime fim, @Param("salaId") Long salaId,
                                      @Param("ignorarOcupacaoId") Long ignorarOcupacaoId);
}
