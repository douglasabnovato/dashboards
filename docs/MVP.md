# MVP — learnTECH OS — Growth Hub

> Fonte única do estado. Tabela atual no topo; as anteriores estão em "Histórico", no fim. Revisto em 16/11/2026, às 12h22: critério 9 com os alertas do Dependabot ligados.

**Nível:** Estudo (N1). Os critérios 5 e 7 são "Não se aplica (N1)" e saem da conta.

**Resumo:** 10 de 10 aplicáveis atendidos.

**Finalizado:** sim. Todos os critérios aplicáveis atendem; o 9 passou a "Atende" em 16/11/2026, depois que o responsável ligou os alertas do Dependabot (#23). Falta também o merge da sprint na `main` (PR `developer/sprint-2026-11-19` → `main`, com `revisao-responsavel`), que é do responsável. A publicação da versão Java segue como decisão dele (#10), fora do MVP.

## Critérios em 16/11/2026 (ciclo de 16/11/2026)

A versão Java (API e web) é estudo local até o responsável decidir a #10; os critérios 3 e 9 contam o portal, que é o que está no ar (decisão D2 de 10/11/2026). O portal não tem tema escuro próprio (nenhum `prefers-color-scheme` no CSS), por isso o axe roda em duas larguras, e não em dois esquemas de cor.

| # | Critério | Situação | Evidência (comando, data, resultado) ou o que falta | Issue |
|---|---|---|---|---|
| 1 | Problema e público claros | Atende | Uma frase, em "Problema e hipótese" abaixo | — |
| 2 | Hipótese a validar | Atende | Hipótese com meta (menos de 2 cliques) e com o "saberemos quando", em "Problema e hipótese" abaixo | — |
| 3 | Fluxo principal completo | Atende | `PORTA_PORTAL=4791 npx playwright test`, 16/11/2026: 13 testes passam no Chromium (abrir o portal, escolher um dashboard, usar o visualizador, trocar pelo seletor, erro e "Tentar de novo", voltar). Os mesmos testes rodam no job `portal` do CI: run [43799414312](https://github.com/douglasabnovato/dashboards/actions/runs/43799414312) da `main`, 16/11/2026, `12 passed`; e run [43809970018](https://github.com/douglasabnovato/dashboards/actions/runs/43809970018) do PR #27, 16/11/2026: `13 passed`. `curl -s -o /dev/null -w "%{http_code}"` em https://douglasabnovato.github.io/dashboards/, 16/11/2026: 200 | — |
| 4 | Escopo enxuto | Atende | "Fora do MVP" abaixo | — |
| 5 | Medição de uso | Não se aplica (N1) | Projeto de estudo | — |
| 6 | Falhas tratadas | Atende | `npx playwright test portal`, 16/11/2026: passa "dashboard que não abre mostra o erro e deixa tentar de novo" (404 simulado, alerta, foco em "Tentar de novo" e nova tentativa), em `tests/portal.spec.mjs`; no CI, passa no run [43809970018](https://github.com/douglasabnovato/dashboards/actions/runs/43809970018) do PR #27, 16/11/2026: `13 passed`. Uma falha local, com a máquina carregada, está na #26. O visualizador mostra "Carregando o dashboard…" enquanto abre | #26 |
| 7 | Desempenho aceitável | Não se aplica (N1) | LCP do portal de 1,09 s em 02/11/2026, só como registro | — |
| 8 | Acessibilidade básica | Atende | `npx playwright test acessibilidade`, 16/11/2026: 7 testes passam. axe sem violações no portal em 1280 px e em 375 px (início, visualizador e erro); axe sem nenhuma violação, inclusive moderada, nos 3 dashboards (#16); todo controle alcançado por Tab tem nome e foco visível; no visualizador, Tab entra no dashboard e Shift+Tab volta à barra. Prova de que o teste pega o defeito: com os 2 dashboards da sprint (sem a correção do #16), `npx playwright test acessibilidade -g "sem violação do axe, nem moderada"`, 16/11/2026: 2 falham, com `heading-order`, `landmark-one-main` e `region` | #16 |
| 9 | Segurança e conformidade | Atende | `gh api "repos/douglasabnovato/dashboards/dependabot/alerts?state=open&per_page=100"`, 16/11/2026 11h59: 0 alertas abertos (alertas ligados pelo responsável nesse dia; o grafo de dependências lê também os `pom.xml`: 16 pacotes Maven no SBOM). `npm audit --omit=dev` depois do `npm ci`, 16/11/2026 12h22: 0 vulnerabilidades. Sem segredo no código: senhas e chaves só por variável (`.env.example` só com nomes). Portal estático, sem dado pessoal; os dados da migração `V2__dados_originais.sql` são métricas e tarefas, sem nome nem e-mail. Números dos dashboards marcados como ilustrativos (decisão D4), com teste | #23 |
| 10 | Entrega confiável | Atende | `gh api repos/douglasabnovato/dashboards/rules/branches/main`, 16/11/2026: `deletion`, `non_fast_forward`, `pull_request` e `required_status_checks`; o ruleset `principal-protegida` exige os jobs `build` e `portal` | — |
| 11 | Testes dos fluxos críticos | Atende | Run [43799414312](https://github.com/douglasabnovato/dashboards/actions/runs/43799414312) da `main`, 16/11/2026: job `build` com `mvn -B verify` (core, api e web, `Skipped: 0`) e job `portal` com `12 passed`, sem `skipped`; job `portal` no run [43809970018](https://github.com/douglasabnovato/dashboards/actions/runs/43809970018) do PR #27, 16/11/2026: `13 passed`, sem `skipped` | — |
| 12 | Documentação honesta | Atende | README e `docs/DEPLOY.md` conferidos em 16/11/2026: dizem que só o portal está no ar (Pages no modo legado, `main` e pasta `/`) e que o CI já roda o `mvn -B verify`; uma tabela só, resumo batendo com ela | — |

### Teclado nos dashboards (09/11/2026, Playwright local, Chromium)

Lista de cada parada do Tab conferida por `tests/acessibilidade.spec.mjs`: todas com nome acessível e foco visível.

- **Analytics v1:** botões "Histórico Real" e "Simulador de Cenários" e os 4 filtros de fase.
- **Analytics v2:** abas "Dashboard & Simulador" e "Guia", modos, 3 presets, 4 filtros de fase e os 4 sliders do simulador (agora com nome).
- **Kanban semanal:** "Reset", filtros de projeto e de dia (com `aria-pressed`), as 5 colunas (roláveis por teclado) e os cartões com "Puxar".

## Problema e hipótese

**Problema e público:** Quem gere tráfego pago analisa KPIs, simula o funil e executa tarefas no mesmo lugar.

**Hipótese (meta decidida pelo coordenador em 10/11/2026, decisão D1, revisável):** Acreditamos que juntar Analisar, Decidir e Executar num só portal faz a pessoa achar o dashboard certo em menos de 2 cliques. Saberemos quando o teste de ponta a ponta abrir cada dashboard com 1 clique a partir do início e uma pessoa de fora repetir isso sem ajuda.

## Próximas tarefas

- Ligar os alertas do Dependabot (responsável, #23); decidir publicar no Render e no Neon ou manter como estudo (responsável, #10).
- Padronizar as fórmulas de ROAS, CPA e CAC (#11); tirar o Tailwind Play CDN dos dashboards (#17); atualizações semanais de dependências (#24); Tailwind 4 no portal (#25); teste do erro estável (#26).

## Fora do MVP

Integração direta com Meta Ads e CRM, publicação da versão Java até o responsável decidir e um tema escuro para o portal.

## Histórico

- 16/11/2026, 12h22: critério 9 de "Parcial" para "Atende". Antes, a API de alertas do Dependabot respondia 403 (alertas desligados, engenharia#33). Com os alertas ligados, 0 abertos e audit de produção 0. Resumo de 9 para 10 de 10.

### Tabela de 10/11/2026 (ciclo da faixa de 90%), substituída em 16/11/2026

**Resumo de então:** 10 de 10. Revisão de 16/11/2026: o critério 9 estava "Atende" só pelo `npm audit`, com o Dependabot desligado, e o critério 8 citava "modo claro e escuro" num portal sem tema escuro. Os dois foram corrigidos na tabela atual.

| # | Critério | Situação | Evidência (comando, data, resultado) ou o que falta | Issue |
|---|---|---|---|---|
| 3 | Fluxo principal completo | Atende | `npx playwright test`, 10/11/2026: 12 testes passam. Job `portal` do CI (PR #22). `curl` no portal, 10/11/2026: 200 | — |
| 8 | Acessibilidade básica | Atende | axe sem violações no portal "em modo claro e escuro" (o portal não tem tema escuro: o escuro repetia o claro); axe sem violação séria ou crítica nos 3 dashboards | #16 |
| 9 | Segurança e conformidade | Atende | `npm audit --omit=dev`, 10/11/2026: 0. Dependabot desligado (403), e o audit foi tomado como fonte | — |
| 10 | Entrega confiável | Atende | `gh api repos/douglasabnovato/dashboards/rules/branches/main`, 10/11/2026: ruleset com `build` e `portal` | #9 |

Os demais critérios (1, 2, 4, 6, 11 e 12) estavam como na tabela atual; 5 e 7, "Não se aplica (N1)".

### Tabela de 10/11/2026, antes do ciclo da faixa de 90% (ciclo de 07/11/2026)

**Resumo:** 8 de 10 aplicáveis atendidos (1, 2, 4, 6, 8, 10, 11 e 12). Os 2 parciais (3 e 9) dependem do responsável.

**Finalizado:** sim (os critérios que o código resolve estão em "Atende"; 3 e 9 têm issue com `depende-do-responsavel`: #10).

### Critérios em 10/11/2026 (ciclo de 07/11/2026)

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

#### Teclado nos dashboards (09/11/2026, Playwright local, Chromium)

Lista de cada parada do Tab conferida por `tests/acessibilidade.spec.mjs`: todas com nome acessível e foco visível.

- **Analytics v1:** botões "Histórico Real" e "Simulador de Cenários" e os 4 filtros de fase.
- **Analytics v2:** abas "Dashboard & Simulador" e "Guia", modos, 3 presets, 4 filtros de fase e os 4 sliders do simulador (agora com nome).
- **Kanban semanal:** "Reset", filtros de projeto e de dia (com `aria-pressed`), as 5 colunas (roláveis por teclado) e os cartões com "Puxar".

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
