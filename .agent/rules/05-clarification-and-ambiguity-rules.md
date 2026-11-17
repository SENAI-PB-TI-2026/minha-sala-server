# Regra de Clarificação de Dúvidas e Ambiguidade (Ask Before Action)

Esta regra é obrigatória para a Engenheira de Prompt e todos os agentes que atuam no projeto `minha-sala-server`. O objetivo é evitar retrabalho, suposições equivocadas e desalinhamento de expectativas.

---

## 1. Princípio Fundamental: Parar e Perguntar

Sempre que uma solicitação, ideia ou instrução do usuário estiver ambígua, incompleta ou gerar qualquer dúvida sobre o resultado esperado:

> 🛑 **PARE imediatamente e faça perguntas antes de executar a tarefa.**

---

## 2. Diretrizes Obrigatórias de Comunicação

1. **Proibido Suposições por Conta Própria**:
   - Não deduza comportamentos de regras de negócio, nomes de campos, relacionamentos de banco ou fluxos de dados cruciais quando eles não tiverem sido especificados.
2. **Perguntas Diretas, Curtas e Objetivas**:
   - Vá direto ao ponto, sem preâmbulos desnecessários ou explicações excessivamente longas.
3. **Listagem em Tópicos (Bullet Points)**:
   - Se houver mais de uma dúvida, liste-as obrigatoriamente em tópicos numerados ou marcadores para que o usuário possa responder item por item com facilidade.
4. **Aguardar Confirmação Antes da Entrega Final**:
   - Não inicie a escrita definitiva de código, criação de migrations ou alterações estruturais sem que o usuário tenha respondido aos pontos de dúvida.

---

## 3. Exemplos Práticos

### Cenário Incorreto ❌:
- **Usuário**: *"Adicione reservas no sistema."*
- **Agente assume sozinho**: Cria campos `dataInicio`, `dataFim`, define que sala pode ter várias reservas, decide tipos por conta própria e gera o código diretamente sem perguntar quem é o responsável, formato de datas ou restrições.

### Cenário Correto ✅:
- **Usuário**: *"Adicione reservas no sistema."*
- **Agente para e pergunta**:
  > *"Antes de iniciar a implementação, preciso alinhar os seguintes pontos:*
  > 
  > • *Qual informação do responsável pela reserva devemos registrar (ex: nome, e-mail ou matrícula)?*
  > • *A reserva precisa de data e hora de início e fim, ou apenas a data do dia?*
  > • *Devemos permitir mais de uma reserva para a mesma sala no mesmo horário ou deve haver validação de conflito?*
  > 
  > *Aguardarei sua confirmação para prosseguir com a implementação."*
