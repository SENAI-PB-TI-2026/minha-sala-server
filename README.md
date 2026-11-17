# Minha Sala Server

Servidor do projeto **Minha Sala**.

## Sobre

Este repositório contém o backend responsável pelos serviços da aplicação Minha Sala.

## Status

Em desenvolvimento.

## API de salas

| Método | Rota | Resultado |
| --- | --- | --- |
| GET | `/salas` | Lista de salas (200) |
| GET | `/salas/{id}` | Consulta por ID (200) |
| POST | `/salas` | Criação (201 e cabeçalho Location) |
| PUT | `/salas/{id}` | Atualização (200) |
| DELETE | `/salas/{id}` | Exclusão (204) |

Corpo para criar ou atualizar:

```json
{"nome": "Sala 01", "capacidade": 30}
```

O nome deve conter texto e a capacidade deve ser positiva. Dados inválidos retornam 400; IDs inexistentes retornam 404. O ID é gerado na criação e definido pela URL na atualização.

## API de turmas

| Método | Rota | Resultado |
| --- | --- | --- |
| GET | `/turmas` | Lista de turmas (200) |
| GET | `/turmas/{id}` | Consulta por ID (200) |
| POST | `/turmas` | Criação (201 e cabeçalho Location) |
| PUT | `/turmas/{id}` | Atualização (200) |
| PATCH | `/turmas/{id}/quantidade-alunos` | Atualiza somente a quantidade de alunos (200) |
| DELETE | `/turmas/{id}` | Exclusão (204) |

Corpo para criar ou atualizar:

```json
{"nome": "Turma A", "curso": "Desenvolvimento de Sistemas", "quantidadeAlunos": 30}
```

O nome e o curso devem conter texto e a quantidade de alunos deve ser positiva. Dados inválidos retornam 400; IDs inexistentes retornam 404. O ID é gerado na criação e definido pela URL na atualização.

Para atualizar somente a quantidade de alunos, envie `PATCH /turmas/{id}/quantidade-alunos` com `{"quantidadeAlunos": 35}`. A operação retorna a turma atualizada; uma consulta `GET /turmas/{id}` apresenta a nova quantidade.

Com JDK 25 configurado no `JAVA_HOME`, execute `./mvnw.cmd spring-boot:run` no Windows. As APIs ficam em `http://localhost:8080/salas` e `http://localhost:8080/turmas` e usam H2 persistido em `./data/minha-sala`.

Execute `./mvnw.cmd test` para verificar o CRUD e as validações com H2 em memória.

## API de aulas

| Método | Rota | Resultado |
| --- | --- | --- |
| GET | `/aulas` | Lista de aulas cadastradas (200) |
| GET | `/aulas/{id}` | Consulta por ID (200) |
| POST | `/aulas` | Cadastro (201 e cabeçalho Location) |

Para cadastrar uma aula prevista, envie turma, docente, data, início e fim:

```json
{"turmaId": 1, "docente": "Ana Souza", "data": "2026-12-10", "inicio": "08:00", "fim": "09:30"}
```

A turma deve existir e o horário de fim deve ser posterior ao início. A aula é persistida sem sala associada.

## Desenvolvimento

### Configuração do banco

Copie `.env.example` para `.env` na raiz do projeto e ajuste `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`. No PowerShell:

```powershell
Copy-Item .env.example .env
```

O Spring carrega o `.env` ao iniciar pela raiz do projeto. Use o formato `CHAVE=valor`, sem `export` e sem aspas ao redor dos valores. As variáveis de ambiente do sistema têm prioridade. O arquivo `.env` fica fora do Git; o `.env.example` contém os valores de exemplo para H2.

As instruções de instalação, configuração e execução serão adicionadas conforme a estrutura do projeto for implementada.

## Licença

Ainda não definida.
