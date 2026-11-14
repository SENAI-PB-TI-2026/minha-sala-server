# Governança de Agentes de IA - Minha Sala Server

Este diretório contém as regras (`rules`), habilidades (`skills`), prompts e instruções operacionais para o time de agentes de inteligência artificial que atuam no repositório `minha-sala-server`.

---

## Papéis

1. **Engenheira de Prompt**:
   - Analisa solicitações e requisitos de negócio.
   - Converte demandas em especificações técnicas detalhadas.
   - Aplica as regras de governança e dispara prompts estruturados para a Desenvolvedora Java.
2. **Desenvolvedora Pleno Especializada em Java (`java_pleno_dev`)**:
   - Desenvolve APIs REST com Java 25 e Spring Boot.
   - Implementa arquitetura em camadas (`controller`, `service`, `repository`, `model`, `dto`).
   - Gerencia migrações de banco de dados com Flyway.
   - Segue estritamente as restrições negativas do projeto.

---

## Estrutura de Documentos

- [`rules/00-project-constraints.md`](rules/00-project-constraints.md): **Restrições críticas** (Sem auth, sem autorização, sem testes automatizados).
- [`rules/01-architecture-and-layers.md`](rules/01-architecture-and-layers.md): Arquitetura e responsabilidades de cada camada.
- [`rules/02-java25-and-spring-standards.md`](rules/02-java25-and-spring-standards.md): Boas práticas com Java 25, Records e Spring Boot.
- [`rules/03-flyway-database-rules.md`](rules/03-flyway-database-rules.md): Políticas de migração e versionamento de banco de dados.
- [`rules/04-git-commit-and-pr-rules.md`](rules/04-git-commit-and-pr-rules.md): **Conventional Commits** e aprovação humana obrigatória para push/Pull Request.
- [`rules/05-clarification-and-ambiguity-rules.md`](rules/05-clarification-and-ambiguity-rules.md): **Parar e perguntar** antes de agir em caso de dúvidas, ideias incompletas ou ambiguidades.
- [`skills/new-crud-feature.md`](skills/new-crud-feature.md): Guia de implementação de novos endpoints CRUD.
- [`skills/db-migration.md`](skills/db-migration.md): Guia de evolução de esquemas relacionais.
- [`skills/prompt-template-delegation.md`](skills/prompt-template-delegation.md): Template oficial de envio de tarefas da Engenheira de Prompt para a Desenvolvedora.
- [`prompts/developer-agent-prompt.md`](prompts/developer-agent-prompt.md): Prompt de sistema da Desenvolvedora Java Pleno.
