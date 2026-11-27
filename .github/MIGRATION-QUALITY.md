# Migration Quality — diagnóstico docente

A CI analisa as migrations SQL comuns em `src/main/resources/db/migration` e as
variantes em `src/main/resources/db/vendor` de forma
automática nos PRs para `develop` e `main`, nos pushes dessas branches e em
execuções manuais. Não utiliza credenciais, banco de produção ou serviços pagos.
Não modifica o Flyway, o POM, as migrations existentes nem exige testes/cobertura.

## Como interpretar os checks

- **Convenção e histórico**: procura nomes fora do padrão do projeto
  `V1.0__cria_tabela.sql` (MAJOR inteiro positivo, MINOR inteiro não negativo,
  sem zeros à esquerda, descrição em snake_case; permite também
  `R__descricao.sql` para repeatables), versões numéricas equivalentes, lacunas
  de sequência, novas versões anteriores à maior versão da base e edição,
  remoção ou renomeação de scripts versionados. No PR, usa a base real e a
  árvore de merge prevista. A sequência começa em V1.0; o MINOR avança de um
  em um, ou o MAJOR avança de um em um e o MINOR volta a zero quando houver
  uma mudança incompatível. Repeatables podem ser atualizadas. Nomes antigos
  ainda são reconhecidos para detectar equivalências e alterações do histórico.
  A sequência é conferida por banco. A relocação de uma migration comum para
  H2 preserva o histórico quando o nome e o conteúdo ficam iguais. Uma variante
  nova para PostgreSQL com versão anterior à maior versão da base gera um
  apontamento para revisão.
- **SQLFluff**: feedback de formatação, palavras-chave e parsing SQL no
  dialeto ANSI, com anotações no check. H2 não tem dialeto específico;
  falsos positivos são possíveis. Não altera ou formata os arquivos.
- **Execução real**: o check existente de build e inicialização aplica o
  Flyway em H2 temporário e usa Hibernate `ddl-auto=validate`.
  É uma validação do esquema para começar do zero, não de upgrade com dados
  existentes, nem homologação em PostgreSQL.

## Regularização inicial e evolução

Durante a regularização inicial do SCRUM-301, a renumeração foi autorizada
porque ainda não há banco persistente a preservar. As comparações com a base
legada podem sinalizar alterações esperadas; as verificações permanecem
**informativas**, sem suprimir os alertas de histórico. Após a regularização,
novas migrations devem seguir a sequência e os scripts já aplicados são imutáveis.
Não renomeie, altere ou apague scripts já aplicados sem antes avaliar
`flyway_schema_history` nos ambientes existentes; não automatize `repair`.

Quando a regularização estiver revisada e os bancos existentes compatíveis,
o docente poderá ativar `MIGRATION_STRICT=true` nas variáveis de Actions
para transformar apenas os problemas de convenção/histórico em falhas de CI.
A configuração Spring `spring.flyway.validate-migration-naming=true` pode ser
adotada depois da mesma regularização, para falhar ao iniciar a aplicação
com nomes inválidos. A CI de lint permanece informativa. Não configure novos
checks obrigatórios antes da homologação.

Posteriormente, testar instalação e atualização em banco populado com dados
fictícios, usando a versão de PostgreSQL efetivamente adotada pelo projeto.
O lint não garante segurança de execução, integridade dos dados ou ausência
de locks.

Referências:
- https://documentation.red-gate.com/flyway/reference/commands/validate
- https://docs.sqlfluff.com/en/stable/usage/ci-cd
