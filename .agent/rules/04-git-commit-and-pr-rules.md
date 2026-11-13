# Regras de Git: Conventional Commits e Aprovação de Pull Request

Este documento estabelece o padrão obrigatório para estruturação de commits e a política de aprovação para push e abertura de Pull Requests no projeto `minha-sala-server`.

---

## 1. Estrutura do Conventional Commit

Todo commit gerado no projeto deve seguir estritamente o formato anatômico do **Conventional Commits**:

```
<tipo>(<escopo>): <descrição>
```

| Elemento | Obrigatoriedade | Descrição e Regras |
| :--- | :--- | :--- |
| **`<tipo>`** | **Obrigatório** | Define a natureza da alteração (ver Vocabulário Semântico abaixo). |
| **`(<escopo>)`** | *Opcional* | Contexto ou módulo afetado entre parênteses. Ex: `(salas)`, `(db)`, `(api)`, `(deps)`. |
| **`:`** | **Obrigatório** | Dois pontos seguidos de um espaço em branco delimitando o prefixo. |
| **`<descrição>`** | **Obrigatório** | Resumo curto escrito **no imperativo**. Ex: *"cria endpoint de busca"*, *"corrige validação de capacidade"*. <br>❌ **Proibido usar particípio ou gerúndio**: não use *"criado"*, *"criando"*, *"corrigido"*. |

### Exemplo de Formato
`feat(salas): cria endpoint de listagem paginada`  
`fix(service): corrige validacao de capacidade negativa`  
`chore(mvn): atualiza plugin do maven compiler`

---

## 2. Vocabulário Semântico Padrão

Utilize exclusivamente os tipos semânticos oficiais:

| Tipo | Semântica | Quando Utilizar no Projeto |
| :--- | :--- | :--- |
| **`feat:`** | Nova funcionalidade adicionada ao projeto | Novos endpoints, novos DTOs, novos services ou migrations de novas tabelas. |
| **`fix:`** | Correção de um bug no código base | Resolução de bugs em regras de negócio, queries incorretas ou tratamentos de exceção. |
| **`refactor:`** | Reestruturação de código sem alterar regra de negócio | Simplificação de métodos, extração de helpers, melhoria de legibilidade. |
| **`docs:`** | Alterações em documentações ou diagramas de modelagem | Modificações no `README.md`, docs de arquitetura ou diagramas. |
| **`test:`** | Adição ou correção de testes automatizados | *(Nota de Projeto: limitado pelas restrições de escopo do projeto em `00-project-constraints.md`)*. |
| **`chore:`** | Atualização de dependências ou arquivos de configuração | Alterações no `pom.xml`, `.gitignore`, scripts do Maven wrapper (`mvnw`), etc. |

---

## 3. Portão de Aprovação Humana Obrigatório (Human-in-the-Loop)

> [!CAUTION]
> **Nenhum agente está autorizado a realizar `push` para branches remotas ou abrir Pull Requests de forma autônoma sem confirmação explícita do usuário.**

### Fluxo Operacional:
1. **Elaboração da Proposta**:
   - Ao finalizar uma alteração no código, o agente formula a mensagem de commit padronizada com base no Conventional Commits.
2. **Solicitação de Confirmação**:
   - O agente apresenta ao usuário:
     - Os arquivos modificados / criados.
     - A mensagem de commit sugerida.
     - A pergunta expressa se o usuário aprova o commit e se autoriza o envio / Pull Request.
3. **Execução Apenas Após o Aval**:
   - Somente após resposta afirmativa do usuário ("sim", "aprovado", "pode enviar"), o agente pode prosseguir com as etapas de git ou PR.
