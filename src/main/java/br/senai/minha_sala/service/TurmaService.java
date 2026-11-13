package br.senai.minha_sala.service;

import br.senai.minha_sala.dto.TurmaRequest;
import br.senai.minha_sala.dto.TurmaResponse;
import br.senai.minha_sala.model.Turma;
import br.senai.minha_sala.repository.TurmaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TurmaService {
    private final TurmaRepository turmaRepository;

    /** Inicializa o serviço com o repositório de persistência de turmas. */
    public TurmaService(TurmaRepository turmaRepository) {
        this.turmaRepository = turmaRepository;
    }

    /** Retorna todas as turmas persistidas. */
    public List<TurmaResponse> listar() {
        return turmaRepository.findAll().stream().map(this::toResponse).toList();
    }

    /** Busca a turma pelo ID e lança uma resposta 404 se ela não existe. */
    public TurmaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    private Turma buscarEntidadePorId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada"));
    }

    /** Persiste uma nova turma, descartando qualquer ID fornecido pelo cliente. */
    @Transactional
    public TurmaResponse criar(TurmaRequest dados) {
        Turma turma = new Turma(null, dados.nome(), dados.curso(), dados.quantidadeAlunos());
        return toResponse(turmaRepository.save(turma));
    }

    /** Atualiza nome, curso e quantidade de alunos de uma turma existente, preservando seu ID. */
    @Transactional
    public TurmaResponse atualizar(Long id, TurmaRequest dados) {
        Turma turma = buscarEntidadePorId(id);
        turma.setNome(dados.nome());
        turma.setCurso(dados.curso());
        turma.setQuantidadeAlunos(dados.quantidadeAlunos());
        return toResponse(turmaRepository.save(turma));
    }

    /** Atualiza somente a quantidade de alunos e retorna os dados persistidos da turma. */
    @Transactional
    public TurmaResponse atualizarQuantidadeAlunos(Long id, int quantidadeAlunos) {
        Turma turma = buscarEntidadePorId(id);
        turma.setQuantidadeAlunos(quantidadeAlunos);
        return toResponse(turmaRepository.save(turma));
    }

    /** Remove uma turma existente; lança uma resposta 404 para ID desconhecido. */
    @Transactional
    public void excluir(Long id) {
        turmaRepository.delete(buscarEntidadePorId(id));
    }

    private TurmaResponse toResponse(Turma turma) {
        return new TurmaResponse(turma.getId(), turma.getNome(), turma.getCurso(), turma.getQuantidadeAlunos());
    }
}
