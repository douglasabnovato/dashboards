# MVP — learnTECH OS — Growth Hub

> Fonte única do estado. Tabela atual no topo; as anteriores estão em "Histórico", no fim.

**Nível:** Estudo (N1). Os critérios 5 e 7 são "Não se aplica (N1)" e saem da conta.

**Resumo:** 8 de 10 aplicáveis atendidos (1, 2, 4, 6, 8, 10, 11 e 12). Os 2 parciais (3 e 9) dependem do responsável.

**Finalizado:** sim (os critérios que o código resolve estão em "Atende"; 3 e 9 têm issue com `depende-do-responsavel`: #10).

## Critérios em 10/11/2026 (ciclo de 07/11/2026)

| # | Critério | Situação | Evidência (comando, data, resultado) ou o que falta | Issue |
|---|---|---|---|---|
| 1 | Problema e público claros | Atende | Uma frase, em "Problema e hipótese" abaixo | — |
| 2 | Hipótese a validar | Atende | Hipótese escrita, com meta de menos de 2 cliques até o dashboard certo (proposta, sem validação) | — |
| 3 | Fluxo principal completo | Parcial | O portal funciona: `curl` em https://douglasabnovato.github.io/dashboards/ deu 200 em 09/11/2026, com HTML igual ao `index.html` da `main`. Fluxo percorrido na tela com Playwright local em 09/11/2026 (abrir o portal, escolher um dashboard, usar o visualizador, trocar pelo seletor e voltar), `npx playwright test`: 11 testes passam. Falta a versão Java no ar, decisão do responsável | #10 |
| 4 | Escopo enxuto | Atende | "Fora do MVP" abaixo | — |
| 5 | Medição de uso | Não se aplica (N1) | Projeto de estudo | — |
| 6 | Falhas tratadas | Atende | Visualizador com "Carregando o dashboard…" e erro com "Tentar de novo" e "Voltar ao início" (`tests/portal.spec.mjs`) | — |
| 7 | Desempenho aceitável | Não se aplica (N1) | LCP do portal de 1,09 s em 02/11/2026, só como registro | — |
| 8 | Acessibilidade básica | Atende | `npx playwright test acessibilidade`, 09/11/2026: 6 testes passam. axe sem violações no portal em modo claro e escuro (início, visualizador e erro); axe sem violação séria ou crítica nos 3 dashboards; em cada dashboard, todo controle alcançado por Tab tem nome e foco visível; no visualizador, Tab entra no dashboard e Shift+Tab volta à barra. Violações moderadas restantes na #16 | #16 |
| 9 | Segurança e conformidade | Parcial | Portal estático, sem dado pessoal. O login existe só na versão Java, que não está no ar | #10 |
| 10 | Entrega confiável | Atende | `gh api repos/douglasabnovato/dashboards/rulesets`, 10/11/2026: ruleset `principal-protegida` ativo (exige PR, bloqueia force push e exclusão); o workflow `ci` (jobs `build` e `portal`) roda em todo PR | #9 |
| 11 | Testes dos fluxos críticos | Atende | `mvn -B verify` (core, api e web) e 11 testes Playwright do portal no CI | — |
| 12 | Documentação honesta | Atende | README diz que só o portal está no ar; uma tabela só, resumo batendo com ela | — |

### Teclado nos dashboards (09/11/2026, Playwright local, Chromium)

Lista de cada parada do Tab conferida por `tests/acessibilidade.spec.mjs`: todas com nome acessível e foco visível.

- **Analytics v1:** botões "Histórico Real" e "Simulador de Cenários" e os 4 filtros de fase.
- **Analytics v2:** abas "Dashboard & Simulador" e "Guia", modos, 3 presets, 4 filtros de fase e os 4 sliders do simulador (agora com nome).
- **Kanban semanal:** "Reset", filtros de projeto e de dia (com `aria-pressed`), as 5 colunas (roláveis por teclado) e os cartões com "Puxar".

## Problema e hipótese

**Problema e público:** Quem gere tráfego pago analisa KPIs, simula o funil e executa tarefas no mesmo lugar.

**Hipótese (proposta, sem validação):** Se Analisar, Decidir e Executar ficarem em um só portal, a pessoa acha o dashboard certo em menos de 2 cliques.

## Próximas tarefas

- Decidir publicar no Render e no Neon ou manter como estudo (responsável, #10).
- Padronizar as fórmulas de ROAS, CPA e CAC (#11); violações moderadas do axe (#16); tirar o Tailwind Play CDN dos dashboards (#17).

## Fora do MVP

Integração direta com Meta Ads e CRM, e publicação da versão Java até o responsável decidir.

## Histórico

### Revisão de 06/11/2026 (ciclo de 03/11/2026)

**Nível:** Estudo (N1). Os critérios 5 (medição) e 7 (desempenho no ar) passam a "Não se aplica (N1)" e saem da conta. **Resumo:** 7 de 10 aplicáveis atendidos (1, 2, 4, 6, 8, 11 e 12) e 3 parciais (3, 9 e 10). **Finalizado:** não.

| # | Situação em 06/11/2026 | Evidência ou o que falta | Issue |
|---|---|---|---|
| 3 | Parcial | O portal funciona no GitHub Pages (200); a versão Java não está publicada (Render 404) | #10 |
| 5 | Não se aplica (N1) | Projeto de estudo | — |
| 6 | Atende | O visualizador mostra "Carregando o dashboard…" e, se o arquivo não abrir, um erro com "Tentar de novo" e "Voltar ao início" (teste em `tests/portal.spec.mjs`) | — |
| 7 | Não se aplica (N1) | LCP do portal de 1,09 s em 02/11/2026, só como registro | — |
| 9 | Parcial | Login só na versão Java, que não está no ar | #10 |
| 10 | Parcial | `main` sem proteção (decisão 3 do processo) | #9 |
| 12 | Atende | README diz que só o portal está no ar | — |

**Para "Finalizado":** os critérios 3, 9 e 10 dependem do responsável, e cada um tem issue aberta com `depende-do-responsavel` (#9, #10). Quando a #10 for decidida, os critérios 3 e 9 podem fechar.

### Revisão de 02/11/2026

**Hipótese (proposta, sem validação):** Se Analisar, Decidir e Executar ficarem em um só portal, a pessoa acha o dashboard certo em menos de 2 cliques.

**Fora do MVP (proposta):** Integração direta com Meta Ads e CRM e publicação da versão Java até o responsável decidir.

**Medição:** Lighthouse 12.8.2 local, móvel, 3 execuções no site publicado. Portal: LCP 1,09 / 0,96 / 1,12 s (mediana 1,09 s; era 3,2 s), acessibilidade 100. A versão Java não está publicada e não foi medida. O resultado varia entre execuções, e não houve teste em celular real.

**Critério 5:** segue sem atender, porque nada coleta uso; depende de uma conta de medição do responsável.

### Situação em 31/10/2026

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

**Resultado em 31/10/2026:** 7 de 12 atendidos (1, 2, 4, 7, 8, 11 e 12). O texto anterior dizia 1 de 12 e não batia com a tabela; corrigido em 06/11/2026.

Tarefas em 31/10/2026:

| # | Tarefa | Situação |
|---|---|---|
| 1 | Ativar o CI que estava em `ci/` | PR aberto |
| 2 | Decidir publicar no Render e no Neon ou manter como estudo | Com o responsável |
