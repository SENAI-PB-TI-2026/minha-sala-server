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

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public List<Sala> listar() {
        return salaService.listar();
    }

    @GetMapping("/{id}")
    public Sala buscarPorId(@PathVariable Long id) {
        return salaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Sala> criar(@Valid @RequestBody Sala sala) {
        Sala criada = salaService.criar(sala);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public Sala atualizar(@PathVariable Long id, @Valid @RequestBody Sala sala) {
        return salaService.atualizar(id, sala);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        salaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
