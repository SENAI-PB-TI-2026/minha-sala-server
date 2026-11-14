# Padrões Técnicos: Java 25 & Spring Boot

Diretrizes de sintaxe, estilo e boas práticas de desenvolvimento no ecossistema Java moderno do projeto.

---

## 1. Padrões de Linguagem (Java 25)

- **Records para DTOs**: Sempre que representar dados de entrada/saída de requisições, utilize `record`.
  ```java
  public record SalaRequest(
      @NotBlank(message = "O nome é obrigatório")
      String nome,
      
      @NotNull(message = "A capacidade é obrigatória")
      @Positive(message = "A capacidade deve ser maior que zero")
      Integer capacidade
  ) {}
  ```
- **Injeção de Dependências**: Sempre utilize construtores explícitos. Não utilize `@Autowired` em propriedades privadas.
  ```java
  @RestController
  @RequestMapping("/salas")
  public class SalaController {
      private final SalaService salaService;

      public SalaController(SalaService salaService) {
          this.salaService = salaService;
      }
  }
  ```
- **Streams e Imutabilidade**:
  - Prefira `.stream().map(this::toResponse).toList()` para gerar coleções imutáveis.
  - Evite mutações de estado desnecessárias.

---

## 2. Padrões Spring Boot

- **Transacionalidade**: Declare `@Transactional(readOnly = true)` na classe de serviço e aplique `@Transactional` em métodos que realizam escrita/alteração no banco.
- **Validação de Entrada**:
  - Adicione `@Valid` aos parâmetros `@RequestBody` em métodos `POST` e `PUT`.
  - Mensagens de erro de validação devem ser concisas e em português.
- **Tratamento de Exceções**:
  - Para recursos não encontrados por ID, lance `ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado")`.

---

## 3. Nomenclatura e Convenções

- Classes em PascalCase: `SalaController`, `SalaService`, `SalaRepository`.
- Métodos e variáveis em camelCase: `buscarPorId`, `capacidadeTotal`.
- Tabelas e colunas em snake_case: `salas`, `capacidade_maxima`.
- Comentários Javadoc sucintos em português em métodos públicos.
