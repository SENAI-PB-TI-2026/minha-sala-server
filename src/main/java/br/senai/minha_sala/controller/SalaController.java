package br.senai.minha_sala.controller;

import br.senai.minha_sala.dto.BloqueioRequest;
import br.senai.minha_sala.dto.BloqueioResponse;
import br.senai.minha_sala.dto.ConsultaOcupacaoSalasResponse;
import br.senai.minha_sala.dto.OcupacaoRequest;
import br.senai.minha_sala.dto.OcupacaoResponse;
import br.senai.minha_sala.dto.SalaRequest;
import br.senai.minha_sala.dto.SalaResponse;
import br.senai.minha_sala.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/salas")
public class SalaController {
    private final SalaService salaService;

    /** Inicializa o controller com o serviço responsável pelas salas. */
    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    /** Consulta salas ativas, bloqueios e ocupações por período. */
    @GetMapping("/ocupacao")
    public ConsultaOcupacaoSalasResponse consultarOcupacao(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return salaService.consultarOcupacaoPorPeriodo(inicio, fim);
    }

    /** Registra uma aula em uma sala e confirma a ocupação no horário cadastrado. */
    @PostMapping("/{salaId}/aulas/{aulaId}")
    public ResponseEntity<OcupacaoResponse> registrarAula(
            @PathVariable Long salaId,
            @PathVariable Long aulaId) {
        OcupacaoResponse ocupacao = salaService.registrarAulaEmSala(salaId, aulaId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location).body(ocupacao);
    }

    /** Cadastra um bloqueio fixo para a sala. */
    @PostMapping("/{salaId}/bloqueios")
    public ResponseEntity<BloqueioResponse> criarBloqueio(
            @PathVariable Long salaId,
            @Valid @RequestBody BloqueioRequest request) {
        BloqueioResponse bloqueio = salaService.criarBloqueio(salaId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(bloqueio.id()).toUri();
        return ResponseEntity.created(location).body(bloqueio);
    }

    /** Cadastra uma ocupação direta na sala. */
    @PostMapping("/{salaId}/ocupacoes")
    public ResponseEntity<OcupacaoResponse> criarOcupacao(
            @PathVariable Long salaId,
            @Valid @RequestBody OcupacaoRequest request) {
        OcupacaoResponse ocupacao = salaService.criarOcupacao(salaId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(ocupacao.id()).toUri();
        return ResponseEntity.created(location).body(ocupacao);
    }

    /** Retorna todas as salas cadastradas. */
    @GetMapping
    public List<SalaResponse> listar() {
        return salaService.listar();
    }

    /** Consulta uma sala pelo ID; retorna 404 quando ela não existe. */
    @GetMapping("/{id}")
    public SalaResponse buscarPorId(@PathVariable Long id) {
        return salaService.buscarPorId(id);
    }

    /** Valida e cria uma sala, retornando 201 e sua URL no cabeçalho Location. */
    @PostMapping
    public ResponseEntity<SalaResponse> criar(@Valid @RequestBody SalaRequest sala) {
        SalaResponse criada = salaService.criar(sala);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    /** Valida os dados e atualiza a sala identificada pela URL. */
    @PutMapping("/{id}")
    public SalaResponse atualizar(@PathVariable Long id, @Valid @RequestBody SalaRequest sala) {
        return salaService.atualizar(id, sala);
    }

    /** Inativa a sala informada pelo ID na URL. */
    @PatchMapping("/{id}/inativar")
    public SalaResponse inativar(@PathVariable Long id) {
        return salaService.inativar(id);
    }

    /** Exclui a sala informada e retorna 204, ou 404 se ela não existe. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        salaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
