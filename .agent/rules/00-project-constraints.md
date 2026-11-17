# Regras de Restrições Críticas do Projeto (Constraints)

Este documento define limites invioláveis de escopo para o projeto `minha-sala-server`.
A Engenheira de Prompt e a Desenvolvedora Java Pleno devem aderir rigorosamente a estas diretrizes em todas as tarefas.

---

## 1. NENHUMA Autenticação (No Auth)
- **Proibição estrita**: Não adicionar nem configurar módulos de segurança (Spring Security, Apache Shiro, OAuth2, etc.).
- **Proibição de tokens**: Não gerar, validar ou exigir JWT, Bearer tokens, cookies de sessão autenticada ou API keys.
- **Sem senhas ou credenciais**: Não criar tabelas, colunas ou entidades para armazenar senhas, hashes BCrypt ou dados de login.
- **Acesso livre**: Todos os endpoints REST devem ser abertos e públicos, sem exigência de cabeçalho `Authorization`.

---

## 2. NENHUMA Autorização / RBAC (No Authorization / Roles)
- **Proibição de permissões**: Não implementar regras de acesso baseadas em perfis, roles ou authorities (`ADMIN`, `USER`, `MANAGER`, etc.).
- **Sem anotações de segurança**: Não utilizar `@PreAuthorize`, `@Secured`, `@RolesAllowed` ou verificações de papel.
- **Sem checagem de propriedade de recursos**: Qualquer cliente consumidor da API tem permissão para listar, consultar, criar, atualizar e excluir recursos.

---

## 3. NENHUM Teste Automatizado (No Automated Tests)
- **Foco exclusivo em código de produção**: Todo esforço de desenvolvimento deve ser concentrado em `src/main/java` e `src/main/resources`.
- **Proibido criar ou alterar testes**: Não criar novas classes ou métodos em `src/test/java`, nem testes unitários, testes de integração, testes MockMvc ou testes de repositório.
- **Não adicionar dependências de teste**: Não adicionar bibliotecas adicionais como Testcontainers, RestAssured ou AssertJ.
- **Validação de funcionamento**: A validação das entregas deve ser feita exclusivamente pela compilação com Maven (`./mvnw.cmd compile`) e revisão de consistência estrutural.
