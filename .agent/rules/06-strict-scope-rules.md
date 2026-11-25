# Regra de Escopo Estrito (Fazer Somente o que Foi Pedido)

Esta regra é obrigatória para a Engenheira de Prompt, a Desenvolvedora Java Pleno e todos os agentes que atuam no repositório `minha-sala-server`. O objetivo é garantir que nenhuma alteração desnecessária, código não solicitado ou complexidade acidental sejam inseridos no projeto.

---

## 1. Princípio Fundamental: Atenção Estrita ao Pedido

> 🎯 **Execute estritamente o que foi solicitado. Nada além, nada aquém.**

Nenhum agente deve adicionar funcionalidades extras, prever requisitos futuros por conta própria ("gold plating" ou especulação) ou realizar modificações fora do escopo explícito da tarefa.

---

## 2. Diretrizes Obrigatórias

1. **Proibido Implementar Funcionalidades Não Solicitadas (Sem Gold Plating)**:
   - Se a tarefa pede a criação de um endpoint específico, crie apenas esse endpoint.
   - Não adicione campos, tabelas, filtros, ordenações, rotas adicionais ou métodos utilitários que não foram solicitados ou acordados.
   - Respeite o princípio **YAGNI** (*You Aren't Gonna Need It*).

2. **Alterações Mínimas e Cirúrgicas (Minimal Changes)**:
   - Modifique única e exclusivamente os arquivos e linhas de código estritamente necessários para cumprir o que foi pedido.
   - Não refatore código existente que não seja o alvo direto da tarefa.
   - Não reordene importações, métodos ou reformate arquivos alheios sem necessidade direta.

3. **Preservação de Código e Contratos Existentes**:
   - Não altere, remova ou quebre contratos de APIs, DTOs, entidades ou regras de negócio existentes, a não ser que a solicitação exija isso explicitamente.
   - Mantenha a compatibilidade com os endpoints e migrations já consolidados.

4. **Sugestões Devem Ser Apenas Sugestões**:
   - Caso identifique uma oportunidade de melhoria, refatoração ou funcionalidade complementar fora do pedido:
     - **Não implemente no código.**
     - Apenas registre como uma sugestão textual para o usuário ao final da resposta.

---

## 3. Exemplos Práticos

### Cenário Incorreto ❌:
- **Solicitação do Usuário**: *"Adicione o campo 'capacidade' na entidade Sala."*
- **Ação do Agente**: Adiciona `capacidade`, mas aproveita para adicionar também `arCondicionado`, cria um método de busca customizado no repositório e refatora classes alheias.
- **Problema**: Violou o escopo estrito ao adicionar atributos e refatorações que não foram pedidos.

### Cenário Correto ✅:
- **Solicitação do Usuário**: *"Adicione o campo 'capacidade' na entidade Sala."*
- **Ação do Agente**:
  - Altera apenas o necessário: adiciona o atributo `capacidade` na entidade, atualiza os DTOs correspondentes, ajusta o mapeamento no service/controller e gera a migration Flyway correspondente.
  - Não mexe em nada além do que foi solicitado.
