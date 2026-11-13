package br.senai.minha_sala.controller;

import br.senai.minha_sala.dto.TurmaRequest;
import br.senai.minha_sala.dto.TurmaResponse;
import br.senai.minha_sala.dto.QuantidadeAlunosRequest;
import br.senai.minha_sala.service.TurmaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {
    private final TurmaService turmaService;

    /** Inicializa o controller com o serviço responsável pelas turmas. */
    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    /** Retorna todas as turmas cadastradas. */
    @GetMapping
    public List<TurmaResponse> listar() {
        return turmaService.listar();
    }

    /** Consulta uma turma pelo ID; retorna 404 quando ela não existe. */
    @GetMapping("/{id}")
    public TurmaResponse buscarPorId(@PathVariable Long id) {
        return turmaService.buscarPorId(id);
    }

    /** Valida e cria uma turma, retornando 201 e sua URL no cabeçalho Location. */
    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@Valid @RequestBody TurmaRequest turma) {
        TurmaResponse criada = turmaService.criar(turma);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    /** Valida os dados e atualiza a turma identificada pela URL. */
    @PutMapping("/{id}")
    public TurmaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TurmaRequest turma) {
        return turmaService.atualizar(id, turma);
    }

    /** Atualiza somente a quantidade de alunos da turma identificada pela URL. */
    @PatchMapping("/{id}/quantidade-alunos")
    public TurmaResponse atualizarQuantidadeAlunos(@PathVariable Long id,
                                                   @Valid @RequestBody QuantidadeAlunosRequest dados) {
        return turmaService.atualizarQuantidadeAlunos(id, dados.quantidadeAlunos());
    }

    /** Exclui a turma informada e retorna 204, ou 404 se ela não existe. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        turmaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
