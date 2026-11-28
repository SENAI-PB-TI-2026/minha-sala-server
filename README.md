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

Use o JDK 25 com `JAVA_HOME` configurado. Para o PostgreSQL, também é necessário ter o Docker em execução com o Docker Compose disponível (`docker compose version`). Execute os comandos a partir da raiz do repositório.

### PostgreSQL com Docker Compose

Copie `.env.example` para `.env` caso ainda não exista. Se já usa um `.env` para H2, acrescente apenas as variáveis `POSTGRES_*` do exemplo.

```powershell
Copy-Item .env.example .env
```

Preencha `POSTGRES_PASSWORD` com uma senha local, por exemplo uma sequência aleatória de letras e números, e ajuste `POSTGRES_DB` e `POSTGRES_USER` se necessário. Se a porta 5432 já estiver em uso, defina outra em `POSTGRES_PORT`. O banco fica acessível apenas em `127.0.0.1`. O Compose e o Spring leem o mesmo `.env` local, que fica fora do Git. Salve o arquivo em UTF-8, no formato `CHAVE=valor`, sem `export` e sem aspas. As variáveis de ambiente do sistema têm prioridade.

Suba o PostgreSQL e aguarde o healthcheck antes de iniciar a aplicação com o perfil `dev-postgres`.

```powershell
docker compose up -d --wait
docker compose ps
./mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev-postgres"
```

No Linux e no macOS, use `./mvnw` no lugar de `./mvnw.cmd`. Na IDE, selecione o perfil Spring `dev-postgres` e use a raiz do projeto como diretório de trabalho. A API fica disponível em `http://localhost:8080`.

| Comando | Efeito |
| --- | --- |
| `docker compose stop` | Para o serviço e preserva o contêiner e os dados. |
| `docker compose down` | Remove o contêiner e a rede do projeto e preserva o volume `postgres-data`. |
| `docker compose down -v` | **Apaga o volume e todos os dados do PostgreSQL local.** Use apenas para descartar o banco, após fazer backup se precisar dos dados. |

`POSTGRES_DB`, `POSTGRES_USER` e `POSTGRES_PASSWORD` configuram a inicialização de um volume vazio. Alterar o `.env` depois disso exige ajustar o banco ou as credenciais no PostgreSQL existente para que correspondam aos novos valores.

### Flyway

Ao iniciar a aplicação com `dev-postgres`, o Flyway aplica os scripts de `src/main/resources/db/migration` no PostgreSQL. O Hibernate valida o esquema com `ddl-auto=validate`. O volume mantém também a tabela `flyway_schema_history`.

H2 e PostgreSQL usam a mesma sequência da SCRUM-301 em `src/main/resources/db/migration`. A `V1.8` usa `ALTER COLUMN descricao SET DATA TYPE VARCHAR(519)` nos dois bancos.

Em bancos que já aplicaram a redação anterior da `V1.8`, o Flyway detecta a mudança de checksum de `787945023` para `1036457528`. A proposta de transição depende da aprovação da equipe. Com a aplicação parada, faça backup do banco e confirme que `ocupacoes.descricao` já é `VARCHAR(519)` e que essa é a única divergência no histórico. Nessas condições, a pessoa responsável pode executar `flyway repair` com as mesmas migrations e a mesma configuração de banco e esquema da aplicação. Valide o histórico e a inicialização antes de voltar a usar o banco. Qualquer outra divergência precisa ser investigada antes do reparo.

Bancos novos aplicam a `V1.8` diretamente. Divergências de numeração de bancos anteriores à SCRUM-301 precisam de avaliação separada pela equipe.

### H2 como banco padrão

Execute `./mvnw.cmd spring-boot:run` sem ativar `dev-postgres`. O Spring usa o H2 persistido em `./data/minha-sala`, configurável pelas variáveis `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` do exemplo.

## Licença

Ainda não definida.
