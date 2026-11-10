package br.senai.minha_sala.service;

import br.senai.minha_sala.model.Sala;
import br.senai.minha_sala.dto.SalaRequest;
import br.senai.minha_sala.dto.SalaResponse;
import br.senai.minha_sala.repository.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SalaService {
    private final SalaRepository salaRepository;

    /** Inicializa o serviço com o repositório de persistência de salas. */
    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    /** Retorna todas as salas persistidas. */
    public List<SalaResponse> listar() {
        return salaRepository.findAll().stream().map(this::toResponse).toList();
    }

    /** Busca a sala pelo ID e lança uma resposta 404 se ela não existe. */
    public SalaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    private Sala buscarEntidadePorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
    }

    /** Persiste uma nova sala, descartando qualquer ID fornecido pelo cliente. */
    @Transactional
    public SalaResponse criar(SalaRequest dados) {
        Sala sala = new Sala(null, dados.nome(), dados.capacidade());
        return toResponse(salaRepository.save(sala));
    }

    /** Atualiza nome e capacidade de uma sala existente, preservando seu ID. */
    @Transactional
    public SalaResponse atualizar(Long id, SalaRequest dados) {
        Sala sala = buscarEntidadePorId(id);
        sala.setNome(dados.nome());
        sala.setCapacidade(dados.capacidade());
        return toResponse(salaRepository.save(sala));
    }

    /** Remove uma sala existente; lança uma resposta 404 para ID desconhecido. */
    @Transactional
    public void excluir(Long id) {
        salaRepository.delete(buscarEntidadePorId(id));
    }

    private SalaResponse toResponse(Sala sala) {
        return new SalaResponse(sala.getId(), sala.getNome(), sala.getCapacidade());
    }
}
