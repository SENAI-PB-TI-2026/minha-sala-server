package br.senai.minha_sala.controller;

import br.senai.minha_sala.dto.AulaRequest;
import br.senai.minha_sala.dto.AulaResponse;
import br.senai.minha_sala.service.AulaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/aulas")
public class AulaController {
    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping
    public List<AulaResponse> listar() {
        return aulaService.listar();
    }

    @GetMapping("/{id}")
    public AulaResponse buscarPorId(@PathVariable Long id) {
        return aulaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AulaResponse> criar(@Valid @RequestBody AulaRequest dados) {
        AulaResponse criada = aulaService.criar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }
}
