package br.senai.minha_sala.service;

import br.senai.minha_sala.model.Sala;
import br.senai.minha_sala.dto.SalaRequest;
import br.senai.minha_sala.dto.SalaResponse;
import br.senai.minha_sala.dto.BloqueioResponse;
import br.senai.minha_sala.dto.ConsultaOcupacaoSalasResponse;
import br.senai.minha_sala.dto.OcupacaoResponse;
import br.senai.minha_sala.repository.BloqueioRepository;
import br.senai.minha_sala.repository.OcupacaoRepository;
import br.senai.minha_sala.repository.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SalaService {
    private final SalaRepository salaRepository;
    private final BloqueioRepository bloqueioRepository;
    private final OcupacaoRepository ocupacaoRepository;

    /** Inicializa o serviço com os repositórios necessários. */
    public SalaService(SalaRepository salaRepository,
                       BloqueioRepository bloqueioRepository,
                       OcupacaoRepository ocupacaoRepository) {
        this.salaRepository = salaRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.ocupacaoRepository = ocupacaoRepository;
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
        Sala sala = new Sala(null, dados.nome(), dados.capacidade(), dados.tipo(), dados.recursos());
        return toResponse(salaRepository.save(sala));
    }

    /** Atualiza nome e capacidade de uma sala existente, preservando seu ID. */
    @Transactional
    public SalaResponse atualizar(Long id, SalaRequest dados) {
        Sala sala = buscarEntidadePorId(id);
        sala.setNome(dados.nome());
        sala.setCapacidade(dados.capacidade());
        sala.setTipo(dados.tipo());
        sala.setRecursos(dados.recursos());
        return toResponse(salaRepository.save(sala));
    }

    /** Remove uma sala existente; lança uma resposta 404 para ID desconhecido. */
    @Transactional
    public void excluir(Long id) {
        salaRepository.delete(buscarEntidadePorId(id));
    }

    /** Consulta salas ativas, bloqueios e ocupações registradas dentro do período especificado. */
    public ConsultaOcupacaoSalasResponse consultarOcupacaoPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As datas de início e fim do período devem ser informadas");
        }
        if (fim.isBefore(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data final deve ser posterior ou igual à data inicial");
        }

        List<SalaResponse> salas = salaRepository.findSalasAtivas().stream()
                .map(this::toResponse)
                .toList();

        List<BloqueioResponse> bloqueios = bloqueioRepository.findByPeriodo(inicio, fim).stream()
                .map(b -> new BloqueioResponse(
                        b.getId(),
                        b.getSala().getId(),
                        b.getSala().getNome(),
                        b.getDataInicio(),
                        b.getDataFim(),
                        b.getMotivo()
                ))
                .toList();

        List<OcupacaoResponse> ocupacoes = ocupacaoRepository.findByPeriodo(inicio, fim).stream()
                .map(o -> new OcupacaoResponse(
                        o.getId(),
                        o.getSala().getId(),
                        o.getSala().getNome(),
                        o.getData(),
                        o.getInicio(),
                        o.getFim(),
                        o.getDescricao()
                ))
                .toList();

        return new ConsultaOcupacaoSalasResponse(inicio, fim, salas, bloqueios, ocupacoes);
    }

    private SalaResponse toResponse(Sala sala) {
        return new SalaResponse(sala.getId(), sala.getNome(), sala.getCapacidade(), sala.getTipo(), sala.getRecursos());
    }
}
