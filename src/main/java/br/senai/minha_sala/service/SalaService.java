package br.senai.minha_sala.service;

import br.senai.minha_sala.model.Sala;
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
    public List<Sala> listar() {
        return salaRepository.findAll();
    }

    /** Busca a sala pelo ID e lança uma resposta 404 se ela não existe. */
    public Sala buscarPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
    }

    /** Persiste uma nova sala, descartando qualquer ID fornecido pelo cliente. */
    @Transactional
    public Sala criar(Sala sala) {
        sala.setId(null);
        return salaRepository.save(sala);
    }

    /** Atualiza nome e capacidade de uma sala existente, preservando seu ID. */
    @Transactional
    public Sala atualizar(Long id, Sala dados) {
        Sala sala = buscarPorId(id);
        sala.setNome(dados.getNome());
        sala.setCapacidade(dados.getCapacidade());
        return salaRepository.save(sala);
    }

    /** Remove uma sala existente; lança uma resposta 404 para ID desconhecido. */
    @Transactional
    public void excluir(Long id) {
        salaRepository.delete(buscarPorId(id));
    }
}
