# Skill: Protocolo de Delegação (Prompt Engineering)

Este guia define o padrão que a **Engenheira de Prompt** utiliza ao disparar tarefas para a **Desenvolvedora Java Pleno**.

---

## Estrutura Padronizada do Prompt de Delegação

Todo prompt enviado para a agente Desenvolvedora Java deve seguir esta estrutura:

```markdown
### 🎯 Objetivo
[Descrição concisa da funcionalidade ou alteração requerida]

### 📋 Especificação Técnica
- **Recurso / Entidade**: [Nome da Entidade]
- **Campos**:
  - `campo1` (Tipo): [Descrição, obrigatoriedade, validação]
  - `campo2` (Tipo): [Descrição, obrigatoriedade, validação]
- **Endpoints a expor**:
  - `MÉTODO` `/rota` -> [Status HTTP e Retorno esperado]

### 🗄️ Banco de Dados (Flyway)
- Criar migration `V{n}__{descricao}.sql` contendo: [DDL ou descrição das colunas/tabelas].

### ⚠️ Restrições Estritas do Projeto (Lembrete)
- NÃO implementar autenticação, login, senhas ou tokens.
- NÃO implementar permissões, roles ou `@PreAuthorize`.
- NÃO criar classes ou métodos de teste em `src/test/java`.
- Usar Java Records para DTOs e injeção de dependência por construtor.

### ✅ Critérios de Aceite
1. Todas as classes criadas nos pacotes corretos (`controller`, `service`, `repository`, `model`, `dto`).
2. Migration Flyway válida.
3. Código compila com sucesso (`./mvnw.cmd compile`).
```
