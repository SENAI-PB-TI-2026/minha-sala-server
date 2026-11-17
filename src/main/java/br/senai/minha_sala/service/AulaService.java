package br.senai.minha_sala.service;

import br.senai.minha_sala.dto.AulaRequest;
import br.senai.minha_sala.dto.AulaResponse;
import br.senai.minha_sala.model.Aula;
import br.senai.minha_sala.model.Turma;
import br.senai.minha_sala.repository.AulaRepository;
import br.senai.minha_sala.repository.TurmaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class AulaService {
    private final AulaRepository aulaRepository;
    private final TurmaRepository turmaRepository;

    public AulaService(AulaRepository aulaRepository, TurmaRepository turmaRepository) {
        this.aulaRepository = aulaRepository;
        this.turmaRepository = turmaRepository;
    }

    public List<AulaResponse> listar() {
        return aulaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AulaResponse buscarPorId(Long id) {
        return toResponse(aulaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aula não encontrada")));
    }

    @Transactional
    public AulaResponse criar(AulaRequest dados) {
        Turma turma = turmaRepository.findById(dados.turmaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada"));
        Aula aula = new Aula(null, turma, dados.docente(), dados.data(), dados.inicio(), dados.fim());
        return toResponse(aulaRepository.save(aula));
    }

    private AulaResponse toResponse(Aula aula) {
        return new AulaResponse(aula.getId(), aula.getTurma().getId(), aula.getDocente(),
                aula.getData(), aula.getInicio(), aula.getFim());
    }
}
