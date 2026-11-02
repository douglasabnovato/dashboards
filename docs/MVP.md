# MVP — learnTECH OS — Growth Hub

> Criado em 31/10/2026, com os critérios usados no `movies`. Situação baseada em `AVALIACAO.md` e na conferência deste ciclo.

## Problema e hipótese

**Problema e público:** Quem gere tráfego pago analisa KPIs, simula o funil e executa tarefas no mesmo lugar.

**Hipótese:** O responsável toma a decisão semanal de campanha usando só esta ferramenta. Proposta inicial, para o responsável confirmar ou trocar. Sem medição, a meta ainda não pode ser conferida.

## 1. Critérios

| # | Critério | Como verificar | Situação em 31/10/2026 |
|---|---|---|---|
| 1 | Problema e público claros | Dá para explicar em uma frase | Atende |
| 2 | Hipótese a validar | Existe uma aposta escrita, com meta | Atende: proposta abaixo |
| 3 | Fluxo principal completo | Uma pessoa conclui sem ajuda | Parcial: portal testado; versão Java não publicada |
| 4 | Escopo enxuto | O que não entra está registrado | Atende: lista abaixo |
| 5 | Medição de uso | A métrica principal é coletada | Não atende: nada coleta uso |
| 6 | Falhas tratadas | Erro, vazio e carregamento têm resposta clara | Parcial: portal sem estado de erro próprio para o visualizador |
| 7 | Desempenho aceitável | LCP de até 2,5 s no celular | Atende: 1,09 s no portal em 02/11/2026 |
| 8 | Acessibilidade básica | Lighthouse e teclado | Atende: Lighthouse 100; Tab sem elemento sem nome ou sem foco, no portal |
| 9 | Segurança e conformidade | Riscos decididos; termos de terceiros cumpridos | Parcial: há login; o Render não foi publicado |
| 10 | Entrega confiável | Publicação só depois de CI verde | Parcial: CI verde em PR; a `main` não é protegida |
| 11 | Testes dos fluxos críticos | Testes automatizados no CI | Atende: testes Java e 3 testes Playwright no CI |
| 12 | Documentação honesta | Qualquer pessoa roda o projeto, e os números estão certos | Atende: README diz o que está publicado e o que não está |

**Resultado:** 1 de 12 critérios atendidos por inteiro. É um julgamento técnico.

## 2. Tarefas

| # | Tarefa | Situação |
|---|---|---|
| 1 | Ativar o CI que estava em `ci/` | PR aberto |
| 2 | Decidir publicar no Render e no Neon ou manter como estudo | Com o responsável |

## 3. Fora do MVP

Integração direta com Meta Ads e CRM. A lista é uma proposta e pode mudar com a decisão do responsável.

## Revisão de 02/11/2026

**Hipótese (proposta, sem validação):** Se Analisar, Decidir e Executar ficarem em um só portal, a pessoa acha o dashboard certo em menos de 2 cliques.

**Fora do MVP (proposta):** Integração direta com Meta Ads e CRM e publicação da versão Java até o responsável decidir.

**Medição:** Lighthouse 12.8.2 local, móvel, 3 execuções no site publicado. Portal: LCP 1,09 / 0,96 / 1,12 s (mediana 1,09 s; era 3,2 s), acessibilidade 100. A versão Java não está publicada e não foi medida. O resultado varia entre execuções, e não houve teste em celular real.

**Critério 5:** segue sem atender, porque nada coleta uso; depende de uma conta de medição do responsável.
