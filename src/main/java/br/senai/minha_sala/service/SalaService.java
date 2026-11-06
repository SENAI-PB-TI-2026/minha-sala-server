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

    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    public List<Sala> listar() {
        return salaRepository.findAll();
    }

    public Sala buscarPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
    }

    @Transactional
    public Sala criar(Sala sala) {
        sala.setId(null);
        return salaRepository.save(sala);
    }

    @Transactional
    public Sala atualizar(Long id, Sala dados) {
        Sala sala = buscarPorId(id);
        sala.setNome(dados.getNome());
        sala.setCapacidade(dados.getCapacidade());
        return salaRepository.save(sala);
    }

    @Transactional
    public void excluir(Long id) {
        salaRepository.delete(buscarPorId(id));
    }
}
