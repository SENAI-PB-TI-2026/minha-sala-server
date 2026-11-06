package br.senai.minha_sala.controller;

import br.senai.minha_sala.model.Sala;
import br.senai.minha_sala.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/salas")
public class SalaController {
    private final SalaService salaService;

    /** Inicializa o controller com o serviço responsável pelas salas. */
    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    /** Retorna todas as salas cadastradas. */
    @GetMapping
    public List<Sala> listar() {
        return salaService.listar();
    }

    /** Consulta uma sala pelo ID; retorna 404 quando ela não existe. */
    @GetMapping("/{id}")
    public Sala buscarPorId(@PathVariable Long id) {
        return salaService.buscarPorId(id);
    }

    /** Valida e cria uma sala, retornando 201 e sua URL no cabeçalho Location. */
    @PostMapping
    public ResponseEntity<Sala> criar(@Valid @RequestBody Sala sala) {
        Sala criada = salaService.criar(sala);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    /** Valida os dados e atualiza a sala identificada pela URL. */
    @PutMapping("/{id}")
    public Sala atualizar(@PathVariable Long id, @Valid @RequestBody Sala sala) {
        return salaService.atualizar(id, sala);
    }

    /** Exclui a sala informada e retorna 204, ou 404 se ela não existe. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        salaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
