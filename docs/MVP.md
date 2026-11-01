# MVP — learnTECH OS — Growth Hub

> Criado em 31/10/2026, com os critérios usados no `movies`. Situação baseada em `AVALIACAO.md` e na conferência deste ciclo.

## Problema e hipótese

**Problema e público:** Quem gere tráfego pago analisa KPIs, simula o funil e executa tarefas no mesmo lugar.

**Hipótese:** O responsável toma a decisão semanal de campanha usando só esta ferramenta. Proposta inicial, para o responsável confirmar ou trocar. Sem medição, a meta ainda não pode ser conferida.

## 1. Critérios

| # | Critério | Como verificar | Situação em 31/10/2026 |
|---|---|---|---|
| 1 | Problema e público claros | Dá para explicar em uma frase | Atende |
| 2 | Hipótese a validar | Existe uma aposta escrita, com meta | Parcial: meta proposta |
| 3 | Fluxo principal completo | Uma pessoa conclui sem ajuda | Não confirmado: o CI do PR é a primeira execução neste ciclo |
| 4 | Escopo enxuto | O que não entra está registrado | Parcial: dashboards HTML antigos convivem com a versão Java |
| 5 | Medição de uso | A métrica principal é coletada | Não atende: nada coleta uso |
| 6 | Falhas tratadas | Erro, vazio e carregamento têm resposta clara | Não revisto neste ciclo |
| 7 | Desempenho aceitável | LCP de até 2,5 s no celular | Não medido neste ciclo |
| 8 | Acessibilidade básica | Lighthouse e teclado | Não medido neste ciclo |
| 9 | Segurança e conformidade | Riscos decididos; termos de terceiros cumpridos | Parcial: há login; o Render não foi publicado |
| 10 | Entrega confiável | Publicação só depois de CI verde | Parcial: CI ativado em PR; sem publicação |
| 11 | Testes dos fluxos críticos | Testes automatizados no CI | Parcial: testes existem em `core`, `api` e `web`; ver o CI do PR |
| 12 | Documentação honesta | Qualquer pessoa roda o projeto, e os números estão certos | Parcial: o README cita endereços do Render como esperados |

**Resultado:** 1 de 12 critérios atendidos por inteiro. É um julgamento técnico.

## 2. Tarefas

| # | Tarefa | Situação |
|---|---|---|
| 1 | Ativar o CI que estava em `ci/` | PR aberto |
| 2 | Decidir publicar no Render e no Neon ou manter como estudo | Com o responsável |

## 3. Fora do MVP

Integração direta com Meta Ads e CRM. A lista é uma proposta e pode mudar com a decisão do responsável.
