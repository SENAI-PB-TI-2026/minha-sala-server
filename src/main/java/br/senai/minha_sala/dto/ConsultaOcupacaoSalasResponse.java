package br.senai.minha_sala.dto;

import java.time.LocalDate;
import java.util.List;

public record ConsultaOcupacaoSalasResponse(
        LocalDate inicio,
        LocalDate fim,
        List<SalaResponse> salas,
        List<BloqueioResponse> bloqueios,
        List<OcupacaoResponse> ocupacoes
) {
}
