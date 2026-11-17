# Arquitetura e Padrão de Camadas

O projeto `minha-sala-server` segue uma arquitetura em camadas clara e desacoplada, orientada a APIs REST com Spring Boot.

---

## 1. Organização dos Pacotes (`br.senai.minha_sala`)

```
br.senai.minha_sala/
├── MinhaSalaApplication.java    # Ponto de entrada da aplicação Spring Boot
├── controller/                  # Endpoints REST (roteamento, validação e status HTTP)
├── service/                     # Regras de negócio e transações de banco de dados
├── repository/                  # Interfaces Spring Data JPA
├── model/                       # Entidades JPA mapeadas para o banco de dados
└── dto/                         # Records imutáveis para transferência de dados (Request / Response)
```

---

## 2. Responsabilidades por Camada

### Controller (`br.senai.minha_sala.controller`)
- Anotado com `@RestController` e `@RequestMapping("/recurso")`.
- **Nunca acessa diretamente o repositório**; delega obrigatoriamente para a camada de `Service`.
- **Nunca recebe nem retorna entidades JPA diretamente**; utiliza exclusivamente DTOs (`*Request` e `*Response`).
- Injeção obrigatória via **construtor explícito** (sem `@Autowired` em campos).
- Mapeamento estrito de códigos de status HTTP:
  - `GET /recursos`: `200 OK` (lista de recursos)
  - `GET /recursos/{id}`: `200 OK` (recurso encontrado)
  - `POST /recursos`: `201 Created` acompanhado do cabeçalho `Location` com a URI do novo recurso
  - `PUT /recursos/{id}`: `200 OK` (recurso atualizado)
  - `DELETE /recursos/{id}`: `204 No Content`
  - Erros de validação: `400 Bad Request` disparado automaticamente pelo Bean Validation (`@Valid`)
  - Recurso inexistente: `404 Not Found`

### Service (`br.senai.minha_sala.service`)
- Anotado com `@Service`.
- Controla as transações com `@Transactional(readOnly = true)` no nível de classe, marcando com `@Transactional` os métodos de escrita (`criar`, `atualizar`, `excluir`).
- Realiza conversões entre DTOs e Entidades (`toResponse`, construtores ou métodos de fábrica).
- Lança `org.springframework.web.server.ResponseStatusException` com `HttpStatus.NOT_FOUND` para entidades não localizadas por ID.

### Repository (`br.senai.minha_sala.repository`)
- Estende `JpaRepository<Entidade, Long>`.
- Foco em operações CRUD e consultas derivadas por nome (`findBy...`).

### Model / Entidades (`br.senai.minha_sala.model`)
- Anotado com `@Entity` e `@Table(name = "tabela")`.
- Chave primária do tipo `Long` com `@Id` e `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
- Uso de Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) ou métodos explícitos.

### DTOs (`br.senai.minha_sala.dto`)
- Implementados obrigatoriamente como **`record`** do Java.
- `*Request`: Contém anotações do Jakarta Bean Validation (`@NotBlank`, `@Positive`, `@NotNull`, etc.).
- `*Response`: Projeção imutável dos dados devolvidos para o cliente, contendo o `id`.
