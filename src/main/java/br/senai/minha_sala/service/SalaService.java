package br.senai.minha_sala.service;

import br.senai.minha_sala.dto.BloqueioRequest;
import br.senai.minha_sala.dto.BloqueioResponse;
import br.senai.minha_sala.dto.ConsultaOcupacaoSalasResponse;
import br.senai.minha_sala.dto.OcupacaoRequest;
import br.senai.minha_sala.dto.OcupacaoResponse;
import br.senai.minha_sala.dto.SalaRequest;
import br.senai.minha_sala.dto.SalaResponse;
import br.senai.minha_sala.model.Aula;
import br.senai.minha_sala.model.Bloqueio;
import br.senai.minha_sala.model.Ocupacao;
import br.senai.minha_sala.model.Sala;
import br.senai.minha_sala.repository.AulaRepository;
import br.senai.minha_sala.repository.BloqueioRepository;
import br.senai.minha_sala.repository.OcupacaoRepository;
import br.senai.minha_sala.repository.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SalaService {
    private final SalaRepository salaRepository;
    private final BloqueioRepository bloqueioRepository;
    private final OcupacaoRepository ocupacaoRepository;
    private final AulaRepository aulaRepository;

    /** Inicializa o serviço com os repositórios necessários. */
    public SalaService(SalaRepository salaRepository,
                       BloqueioRepository bloqueioRepository,
                       OcupacaoRepository ocupacaoRepository,
                       AulaRepository aulaRepository) {
        this.salaRepository = salaRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.ocupacaoRepository = ocupacaoRepository;
        this.aulaRepository = aulaRepository;
    }

    /** Retorna todas as salas persistidas. */
    public List<SalaResponse> listar() {
        return salaRepository.findAll().stream().map(this::toResponse).toList();
    }

    /** Consulta as salas livres que comportam a turma e possuem o recurso solicitado. */
    public List<SalaResponse> consultarDisponiveis(LocalDate data, LocalTime inicio, LocalTime fim,
                                                  int quantidadeAlunos, String recursoObrigatorio) {
        if (!fim.isAfter(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O horário de fim deve ser posterior ao início");
        }

        Map<Long, String> conflitos = buscarConflitos(data, inicio, fim, null, null);
        return salaRepository.findByAtivoTrue().stream()
                .filter(sala -> !conflitos.containsKey(sala.getId()))
                .filter(sala -> comportaTurma(sala, quantidadeAlunos))
                .filter(sala -> possuiRecurso(sala, recursoObrigatorio))
                .map(this::toResponse)
                .toList();
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
        Sala sala = new Sala(null, dados.nome(), dados.capacidade(), dados.tipo(), dados.recursos(), true);
        return toResponse(salaRepository.save(sala));
    }

    /** Atualiza nome e capacidade de uma sala existente, preservando seu ID. */
    @Transactional
    public SalaResponse atualizar(Long id, SalaRequest dados) {
        Sala sala = salaRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        sala.setNome(dados.nome());
        sala.setCapacidade(dados.capacidade());
        sala.setTipo(dados.tipo());
        sala.setRecursos(dados.recursos());
        return toResponse(salaRepository.save(sala));
    }

    /** Inativa uma sala existente e retorna seus dados atualizados. */
    @Transactional
    public SalaResponse inativar(Long id) {
        Sala sala = salaRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        sala.setAtivo(false);
        return toResponse(salaRepository.save(sala));
    }

    /** Remove uma sala existente; lança uma resposta 404 para ID desconhecido. */
    @Transactional
    public void excluir(Long id) {
        salaRepository.delete(buscarEntidadePorId(id));
    }

    /** Consulta salas (inclusive inativas), bloqueios e ocupações registradas dentro do período especificado. */
    public ConsultaOcupacaoSalasResponse consultarOcupacaoPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As datas de início e fim do período devem ser informadas");
        }
        if (fim.isBefore(inicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data final deve ser posterior ou igual à data inicial");
        }

        List<SalaResponse> salas = salaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();

        List<BloqueioResponse> bloqueios = bloqueioRepository.findByPeriodo(inicio, fim).stream()
                .map(b -> new BloqueioResponse(
                        b.getId(),
                        b.getSala().getId(),
                        b.getSala().getNome(),
                        b.getDataInicio(),
                        b.getInicio(),
                        b.getFim(),
                        b.getMotivo()
                ))
                .toList();

        List<OcupacaoResponse> ocupacoes = ocupacaoRepository.findByPeriodo(inicio, fim).stream()
                .map(this::toOcupacaoResponse)
                .toList();

        return new ConsultaOcupacaoSalasResponse(inicio, fim, salas, bloqueios, ocupacoes);
    }

    /** Cadastra um bloqueio fixo para a sala. */
    @Transactional
    public BloqueioResponse criarBloqueio(Long salaId, BloqueioRequest dados) {
        Sala sala = salaRepository.findByIdForUpdate(salaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        if (!ocupacaoRepository.buscarIdsSalasOcupadas(dados.data(), dados.inicio(), dados.fim(), salaId, null).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível bloquear a sala: existe uma ocupação no horário informado");
        }
        if (!bloqueioRepository.buscarIdsSalasBloqueadas(dados.data(), dados.inicio(), dados.fim(), salaId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe bloqueio sobreposto para a sala neste horário");
        }
        Bloqueio bloqueio = new Bloqueio(null, sala, dados.data(), dados.data(), dados.inicio(), dados.fim(), dados.motivo());
        Bloqueio salvo = bloqueioRepository.save(bloqueio);
        return new BloqueioResponse(salvo.getId(), sala.getId(), sala.getNome(), salvo.getDataInicio(), salvo.getInicio(), salvo.getFim(), salvo.getMotivo());
    }

    /** Cadastra uma ocupação direta na sala validando bloqueios e sobreposições. */
    @Transactional
    public OcupacaoResponse criarOcupacao(Long salaId, OcupacaoRequest dados) {
        Sala sala = salaRepository.findByIdForUpdate(salaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        validarSalaAtiva(sala);
        if (!comportaTurma(sala, dados.quantidadeAlunos())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A capacidade da sala (" + sala.getCapacidade()
                            + " lugares) é insuficiente para a quantidade de alunos informada ("
                            + dados.quantidadeAlunos() + " alunos)"
            );
        }
        validarConflitos(salaId, dados.data(), dados.inicio(), dados.fim(), null);
        Ocupacao ocupacao = new Ocupacao(null, sala, null, dados.data(), dados.inicio(), dados.fim(), dados.descricao());
        return toOcupacaoResponse(ocupacaoRepository.save(ocupacao));
    }

    /** Registra uma aula em uma sala, confirmando a ocupação no horário cadastrado. */
    @Transactional
    public OcupacaoResponse registrarAulaEmSala(Long salaId, Long aulaId) {
        Aula aula = aulaRepository.findByIdForUpdate(aulaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aula não encontrada"));
        Sala sala = salaRepository.findByIdForUpdate(salaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));

        Optional<Ocupacao> ocupacaoExistente = ocupacaoRepository.findByAulaId(aulaId);
        if (ocupacaoExistente.isPresent()) {
            Ocupacao existente = ocupacaoExistente.get();
            if (!existente.getSala().getId().equals(salaId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "A aula já está registrada em outra sala");
            }
            return toOcupacaoResponse(existente);
        }

        validarSalaAtiva(sala);
        if (aula.getTurma() != null && !comportaTurma(sala, aula.getTurma().getQuantidadeAlunos())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A capacidade da sala (" + sala.getCapacidade()
                            + " lugares) é insuficiente para a quantidade de alunos da turma ("
                            + aula.getTurma().getQuantidadeAlunos() + " alunos)"
            );
        }

        validarRecursoObrigatorio(sala, aula.getRecursoObrigatorio());
        LocalDate data = aula.getData();
        LocalTime inicio = aula.getInicio();
        LocalTime fim = aula.getFim();

        // Verifica se a sala está bloqueada nesta data
        if (!bloqueioRepository.buscarIdsSalasBloqueadas(data, inicio, fim, salaId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A sala está bloqueada neste horário");
        }

        // Verifica conflito de horário com outras ocupações na mesma sala e dia
        if (!ocupacaoRepository.buscarIdsSalasOcupadas(data, inicio, fim, salaId, null).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A sala já possui ocupação cadastrada para este horário");
        }

        Ocupacao ocupacao = new Ocupacao();
        ocupacao.setSala(sala);
        ocupacao.setAula(aula);
        ocupacao.setData(data);
        ocupacao.setInicio(inicio);
        ocupacao.setFim(fim);
        String nomeTurma = (aula.getTurma() != null) ? aula.getTurma().getNome() : "";
        ocupacao.setDescricao("Aula: " + nomeTurma + " (" + aula.getDocente() + ")");

        return toOcupacaoResponse(ocupacaoRepository.save(ocupacao));
    }

    private void validarSalaAtiva(Sala sala) {
        if (!sala.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é possível registrar ocupação para uma sala inativa");
        }
    }

    private boolean comportaTurma(Sala sala, int quantidadeAlunos) {
        return quantidadeAlunos <= sala.getCapacidade();
    }

    private boolean possuiRecurso(Sala sala, String recursoObrigatorio) {
        if (recursoObrigatorio == null || recursoObrigatorio.isBlank()) {
            return true;
        }

        String recursoExigido = normalizarNomeRecurso(recursoObrigatorio);
        String recursos = sala.getRecursos();
        return recursos != null && Arrays.stream(recursos.split("[,;\\r\\n]+"))
                .map(SalaService::normalizarNomeRecurso)
                .anyMatch(recursoExigido::equals);
    }

    private void validarRecursoObrigatorio(Sala sala, String recursoObrigatorio) {
        if (!possuiRecurso(sala, recursoObrigatorio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A sala não possui o recurso obrigatório: " + recursoObrigatorio);
        }
    }

    private static String normalizarNomeRecurso(String recurso) {
        String nome = recurso.strip()
                .replaceFirst("^[1-9]\\d*\\s+", "")
                .toLowerCase(Locale.ROOT);
        return switch (nome) {
            case "computadores" -> "computador";
            case "projetores" -> "projetor";
            default -> nome;
        };
    }

    /** Valida se a sala possui bloqueio fixo ou ocupação sobreposta no horário solicitado. */
    private void validarConflitos(Long salaId, LocalDate data, LocalTime inicio, LocalTime fim, Long ignorarOcupacaoId) {
        String motivo = buscarConflitos(data, inicio, fim, salaId, ignorarOcupacaoId).get(salaId);
        if (motivo != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, motivo);
        }
    }

    private Map<Long, String> buscarConflitos(LocalDate data, LocalTime inicio, LocalTime fim,
                                              Long salaId, Long ignorarOcupacaoId) {
        Map<Long, String> conflitos = new HashMap<>();
        for (Long id : bloqueioRepository.buscarIdsSalasBloqueadas(data, inicio, fim, salaId)) {
            conflitos.put(id, "A sala possui bloqueio ativo no horário informado");
        }
        for (Long id : ocupacaoRepository.buscarIdsSalasOcupadas(data, inicio, fim, salaId, ignorarOcupacaoId)) {
            conflitos.putIfAbsent(id, "Ocupação sobreposta não permitida para o mesmo local e horário");
        }
        return conflitos;
    }

    private OcupacaoResponse toOcupacaoResponse(Ocupacao o) {
        Long aulaId = (o.getAula() != null) ? o.getAula().getId() : null;
        return new OcupacaoResponse(
                o.getId(),
                o.getSala().getId(),
                o.getSala().getNome(),
                aulaId,
                o.getData(),
                o.getInicio(),
                o.getFim(),
                o.getDescricao()
        );
    }

    private SalaResponse toResponse(Sala sala) {
        return new SalaResponse(sala.getId(), sala.getNome(), sala.getCapacidade(), sala.getTipo(), sala.getRecursos(), sala.isAtivo());
    }
}
