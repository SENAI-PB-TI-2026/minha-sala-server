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
| PATCH | `/salas/{id}/inativar` | Inativa a sala (200) |
| DELETE | `/salas/{id}` | Exclusão (204) |

Corpo para criar ou atualizar:

```json
{
  "nome": "Sala 01",
  "capacidade": 30,
  "tipo": "LABORATORIO",
  "recursos": "1 projetor, 30 computadores"
}
```

O nome deve conter texto, a capacidade deve ser positiva, o tipo é obrigatório (`INDEFINIDO`, `SALA_DE_AULA` ou `LABORATORIO`) e os recursos são opcionais (máximo de 1000 caracteres). Dados inválidos retornam 400; IDs inexistentes retornam 404. O ID é gerado na criação e definido pela URL na atualização. Para inativar uma sala, envie `PATCH /salas/{id}/inativar`. A operação retorna a sala atualizada com `ativo: false`.

Separe os itens de `recursos` por vírgula, ponto e vírgula ou quebra de linha. Cada item pode começar com uma quantidade inteira positiva seguida de espaço, como `30 computadores`. Para verificar a compatibilidade, a API ignora o prefixo de quantidade, os espaços nas pontas e diferenças de maiúsculas e minúsculas nos dois campos. Os nomes `computador` e `computadores` são equivalentes, assim como `projetor` e `projetores`. Os demais nomes devem coincidir por inteiro após a normalização.

## Bloqueios de sala

Para registrar um período reservado, envie `POST /salas/{salaId}/bloqueios` com a data, os horários e o motivo confirmado pela unidade. `motivo` aceita somente `SESI` ou `EJA`:

```json
{"data":"2026-12-10","inicio":"08:00","fim":"12:00","motivo":"SESI"}
```

O fim deve ser posterior ao início. O bloqueio impede novas ocupações apenas quando elas se sobrepõem ao intervalo cadastrado; intervalos adjacentes são permitidos. A criação retorna 201 e motivos diferentes de `SESI` e `EJA` retornam 400. Bloqueios antigos, anteriores ao cadastro dos horários, continuam ocupando o dia inteiro.

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

Use o campo opcional `recursoObrigatorio` para informar um único recurso, por exemplo `"recursoObrigatorio": "Computador"`. Ao registrar uma nova ocupação para a aula, a API retorna HTTP 400 se a sala não oferece esse recurso. A quantidade informada no cadastro da sala não é comparada ao número de alunos nessa validação.

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
