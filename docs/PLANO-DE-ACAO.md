# Plano de ação — learnTECH OS / Growth Hub (Java)

Timebox do ciclo: 2 h.

| MoSCoW | Item | Ganho | Esforço | Status |
|---|---|---|---|---|
| Must | Core com fases, KPIs, simulador, ROAS e WIP (D1, D2, D4, D5) | Alto | Médio | ✅ 17 testes |
| Must | API com dados originais via Flyway (D6, D7) | Alto | Médio | ✅ migrações validadas no PostgreSQL |
| Must | Web com as 5 telas | Alto | Alto | ✅ escrito; ⏳ executar |
| Must | Proteger o Kanban (D9) | Alto | Baixo | ✅ escrito; ⏳ executar |
| Must | `mvn verify` verde na sua máquina | Alto | Baixo | ⏳ AÇÃO SUA |
| Should | Deploy Render + Neon, CI no GitHub Actions | Médio | Baixo | ⏳ AÇÃO SUA |
| Should | axe e Lighthouse nas 5 telas | Médio | Baixo | Próximo ciclo |
| Could | Arrastar e soltar no Kanban (p:dashboard ou p:draggable) | Médio | Médio | Próximo ciclo |
| Could | Cadastro de meses e de tarefas pela tela | Médio | Médio | Próximo ciclo |
| Could | Tema escuro (vela/arya) como no original | Baixo | Baixo | Próximo ciclo |
| Won't | Integração automática com Meta Ads / Windsor.ai | Alto | Alto | Fora do escopo |

## Ciclo de 16/11/2026

> Branch `feature/2026-11-16` → `developer/sprint-2026-11-19` → `main` (o PR para a `main` fica com o responsável). Nível: Estudo (N1), 10 critérios aplicáveis. O ciclo anterior (PR #20) entrou na `main` em 16/11/2026.

### Produto

**Problema e público.** Sem mudança: quem gere tráfego pago analisa KPIs, simula o funil e organiza as tarefas da semana num só portal. O público é o autor e quem avalia o portfólio.

**Hipótese e meta.** Acreditamos que juntar Analisar, Decidir e Executar num só portal faz a pessoa achar o dashboard certo em menos de 2 cliques. Saberemos quando o teste de ponta a ponta abrir cada dashboard com 1 clique a partir do início, e uma pessoa de fora repetir isso sem ajuda (N1: sem medição de uso).

**O que a pesquisa trouxe.**

| Ideia | O que levamos para o projeto | Fonte |
|---|---|---|
| Toda página precisa de uma região `main`, e todo conteúdo deve ficar dentro de alguma região; leitores de tela pulam de região em região | O Analytics v1 ganhou `<main id="conteudo">` com os cartões, os gráficos e a tabela | [W3C WAI-ARIA APG: regiões (landmarks)](https://www.w3.org/WAI/ARIA/apg/practices/landmark-regions/), [Deque: regra `region` do axe](https://dequeuniversity.com/rules/axe/4.10/region) |
| Os títulos devem descer um nível por vez (h1 → h2 → h3); pular nível confunde quem navega por títulos | Títulos dos gráficos e das colunas do Kanban passaram de h3 para h2, e os cartões do Kanban de h4 para h3 | [Deque: regra `heading-order` do axe](https://dequeuniversity.com/rules/axe/4.10/heading-order) |
| `page.emulateMedia({ colorScheme })` só muda a tela se o CSS tiver `prefers-color-scheme` | O portal não tem tema escuro próprio. O teste "claro e escuro" repetia a mesma tela; virou desktop (1280 px) e celular (375 px), com um teste que confere a falta de `prefers-color-scheme` | [Playwright: `page.emulateMedia`](https://playwright.dev/docs/api/class-page#page-emulate-media), [MDN: `prefers-color-scheme`](https://developer.mozilla.org/docs/Web/CSS/@media/prefers-color-scheme) |
| Os alertas do Dependabot são ligados em Settings → Code security, pela conta dona do repositório | O critério 9 fica "Parcial" até o responsável ligar os alertas (regra em decisão na engenharia#33) | [GitHub Docs: configurar alertas do Dependabot](https://docs.github.com/en/code-security/dependabot/dependabot-alerts/configuring-dependabot-alerts) |

**Backlog priorizado (impacto × esforço).**

| Prioridade | Item | Impacto | Esforço | Destino |
|---|---|---|---|---|
| 1 | Critério 9 honesto: Dependabot desligado (403) deixa "Parcial" | Alto | Baixo | Este ciclo |
| 2 | Tirar o "axe no escuro" falso do teste e do `MVP.md` | Alto | Baixo | Este ciclo |
| 3 | `@axe-core/playwright` 4.14.0 e `@playwright/test` 1.65.1 (só desenvolvimento) | Médio | Baixo | Este ciclo |
| 4 | Violações moderadas do axe nos dashboards (#16) | Médio | Baixo | Este ciclo |
| 5 | `DEPLOY.md` e README com o estado real (CI já roda o `mvn verify`; Pages no modo legado) | Médio | Baixo | Este ciclo |
| 6 | Ligar os alertas do Dependabot | Alto | Baixo | Responsável (issue nova) |
| 7 | Publicar ou não a versão Java | Médio | Médio | Responsável (#10) |
| 8 | Fórmulas iguais em todas as telas; Tailwind CDN nos dashboards | Médio | Médio | #11, #17 |

**Fora do escopo.** Integração com Meta Ads e CRM; medição de uso (N1); tema escuro novo para o portal.

### Engenharia

**Arquitetura.** Portal estático (`index.html` com Tailwind compilado em `assets/portal.css`) no GitHub Pages, modo legado (branch `main`, pasta `/`), que abre 3 dashboards HTML num iframe. À parte, a versão Java (`core`, `api` em Spring Boot e `web` em JSF), testada no CI pelo `mvn -B verify` e não publicada (#10).

**Riscos.**
- Os dashboards ainda pedem o Tailwind e o Chart.js por CDN (#17): os testes deles precisam de rede.
- Sem os alertas do Dependabot, só o `npm audit` olha as dependências do portal; o Maven fica sem alerta.
- Teste do erro com "Tentar de novo" falhou 1 vez em 3 rodadas locais com a máquina carregada e passou sozinho; o CI decide.

**Qualidade.**
- Testes: 13 testes Playwright (eram 12). O axe dos dashboards passa a barrar também as violações moderadas.
- Acessibilidade: `main` e ordem de títulos nos dashboards; axe do portal em desktop e celular.
- Segurança: `npm audit --omit=dev` sem vulnerabilidades; o portal não tem dependência de produção.

**Definição de pronto.** Jobs `build` e `portal` verdes no PR; `MVP.md` com cada "Atende" citando comando, data e resultado; revisão independente feita antes do merge na sprint.

### Plano de ação

**Este ciclo executa.**
1. Dependências de teste no patch e minor, com porta do portal por variável (`PORTA_PORTAL`).
2. `main` e títulos nos dashboards, com o axe barrando violações moderadas (#16).
3. Teste do portal em larguras, no lugar do "escuro" falso.
4. `MVP.md`, README e `DEPLOY.md` com o estado real.

**Vira issue.** Ligar os alertas do Dependabot e mesclar o ciclo (responsável); atualizações semanais de dependências; Tailwind 4 no portal; estabilidade do teste de erro. Seguem #10, #11 e #17.

## Ciclo de 03/11/2026

> Executado em 06/11/2026. Branch `feature/2026-11-06` → `developer/sprint-2026-11-12` → `main`. Nível: Estudo (N1); critérios 5 e 7 não se aplicam.

### Produto

**Problema e público.** Quem gere tráfego pago analisa KPIs, simula o funil e organiza as tarefas da semana num só lugar. O público é o próprio autor e quem avalia o portfólio.

**Hipótese e meta.** Se Analisar, Decidir e Executar ficarem num só portal, a pessoa acha o dashboard certo em menos de 2 cliques. Meta proposta, sem medição (N1).

**O que a pesquisa trouxe.**

| Ideia | O que levamos para o projeto | Fonte |
|---|---|---|
| O iframe não avisa quando a página dentro dele dá 404; só uma requisição feita por script vê o status | O visualizador faz um `HEAD` no arquivo antes de abrir. Se falhar, mostra o erro com "Tentar de novo" e "Voltar ao início" | [Stack Overflow (espelho): detectar 404 em iframe](https://dev.library.kiwix.org/content/stackoverflow_en_nopic_2021-08/questions/16499117/how-to-detect-an-error-404-in-an-iframe), [Treehouse: erro de carga em iframe](https://teamtreehouse.com/community/check-iframe-loaderror) |
| Todo iframe precisa de `title` para leitores de tela | O iframe ganhou `title="Dashboard selecionado"` | [Equalize Digital: iframe sem título](https://equalizedigital.com/accessibility-checker/iframe-missing-title/) |
| Painéis de tráfego pago perdem a confiança quando a mesma métrica tem fórmulas diferentes | Virou a #11: uma tabela de fórmulas e testes que comparam v1, v2 e o `core` | [Stellar Search: KPIs de mídia paga](https://www.stellarsearch.co.uk/insight/10-kpis-every-paid-marketing-dashboard-needs), [Databox: KPIs de anúncios](https://databox.com/most-important-advertising-kpis) |

**Backlog priorizado (impacto × esforço).**

| Prioridade | Item | Impacto | Esforço | Destino |
|---|---|---|---|---|
| 1 | Estado de carregando e erro no visualizador | Alto | Baixo | Este ciclo |
| 2 | Resumo do `MVP.md` batendo com a tabela e régua N1 | Alto | Baixo | Este ciclo |
| 3 | Playwright 1.65.0, que instala o Chromium no `ubuntu-latest` atual | Alto | Baixo | Este ciclo |
| 4 | Proteger a `main` | Alto | Baixo | Responsável (#9) |
| 5 | Publicar ou não a versão Java | Médio | Médio | Responsável (#10) |
| 6 | Fórmulas iguais em todas as telas | Médio | Médio | Issue (#11) |
| 7 | axe no CI | Médio | Baixo | Issue (#12) |
| 8 | Teclado dentro dos dashboards | Médio | Médio | Issue (#13) |

**Fora do escopo.** Integração com Meta Ads e CRM; medição de uso (N1).

### Engenharia

**Arquitetura.** Portal estático (`index.html` com Tailwind compilado em `assets/portal.css`) publicado no GitHub Pages, que abre os dashboards HTML num iframe. À parte, a versão Java: `core` (regras), `api` (Spring Boot, H2 ou PostgreSQL) e `web` (JSF/PrimeFaces), pensada para o Render e o Neon, mas não publicada.

**Riscos.**
- `https://learntech-dashboards-web.onrender.com` respondeu 404 em 06/11/2026: o serviço não existe (#10).
- Sem proteção na `main`, um push direto publica o portal sem CI (#9).
- `assets/portal.css` é gerado e versionado; esquecer o `npm run css` deixa classes novas sem estilo.

**Qualidade.**
- Testes: `mvn -B verify` e 5 testes Playwright no portal (2 novos: erro com "Tentar de novo", e o último dashboard escolhido vence).
- Acessibilidade: o erro usa `role="alert"` e leva o foco para "Tentar de novo"; o carregando usa `role="status"`; botões com foco visível.
- Desempenho: o `HEAD` é uma requisição pequena ao mesmo domínio. Fora da régua N1.
- Segurança: nada novo sai do navegador.

**Definição de pronto.** `mvn -B verify`, `npm run links` e `npm test` verdes no CI; `MVP.md` e README atualizados.

### Plano de ação

**Este ciclo executa.**
1. Carregando e erro no visualizador, com 2 testes.
2. Playwright 1.65.0.
3. `MVP.md` pela régua N1, com o resumo batendo; README com estado e próximos passos.

**Vira issue.** #9 e #10 (responsável); #11, #12 e #13 (agente).

## Ciclo de 10/11/2026 (faixa de 90%)

> Branch `feature/2026-11-10-b` → `developer/sprint-2026-11-12` → `main` (o PR para a `main` fica com o responsável). Decisões D1 a D7 em `engenharia/decisoes/2026-11-10-ciclo-faixa-90.md`.

**Produto.** Sem mudança de problema e público. A meta da hipótese passa a ser a do ciclo (D1).

**Engenharia.** Portal estático no Pages com testes Playwright no CI. Risco tratado: o card do Analytics v1 dizia "dados auditados" e "indicadores reais" sem confirmação; agora os números aparecem como ilustrativos (D4), com teste.

**Plano de ação.**

| Item | Destino |
|---|---|
| Aviso de números ilustrativos, com teste | Este ciclo |
| Critérios 2, 3, 9 e 10 pelas decisões D1 a D4 | Este ciclo |
| Publicar a versão Java | Responsável (#10) |
| Fórmulas, axe moderado, Tailwind CDN | #11, #16, #17 |

## Ciclo de 07/11/2026

> Executado em 09/11/2026. Branch `feature/2026-11-07` → `developer/sprint-2026-11-12` → `main` (o PR para a `main` fica com o responsável). Nível: Estudo (N1), 10 critérios aplicáveis.

### Produto

**Problema e público.** Sem mudança: quem gere tráfego pago analisa KPIs, simula o funil e executa tarefas no mesmo portal.

**O que a pesquisa trouxe.**

| Ideia | O que levamos para o projeto | Fonte |
|---|---|---|
| Botão que liga e desliga um estado deve informar esse estado com `aria-pressed` | Os filtros de projeto e de dia do Kanban passaram a dizer qual está ativo | [WAI-ARIA APG: Button Pattern](https://www.w3.org/WAI/ARIA/apg/patterns/button/) |
| O Tailwind Play CDN é só para desenvolvimento; em produção, o CSS sai do CLI ou de um build | O portal já usa o CLI; os 3 dashboards antigos ainda usam o CDN. Virou a #17 | [Tailwind: Play CDN](https://tailwindcss.com/docs/installation/play-cdn) |

**Backlog priorizado (impacto × esforço).**

| Prioridade | Item | Impacto | Esforço | Destino |
|---|---|---|---|---|
| 1 | axe no CI, no portal e nos dashboards (#12) | Médio | Baixo | Este ciclo |
| 2 | Teclado nos 3 dashboards (#13) | Médio | Baixo | Este ciclo |
| 3 | Corrigir o que o axe e o teclado acharem | Médio | Baixo | Este ciclo |
| 4 | `MVP.md` com uma tabela só | Alto | Baixo | Este ciclo |
| 5 | Violações moderadas restantes | Baixo | Baixo | #16 |
| 6 | Tirar o Play CDN dos dashboards | Médio | Médio | #17 |
| 7 | Publicar a versão Java, proteger a `main` | Alto | Baixo | Responsável (#10, #9) |

**Fora do escopo.** Reescrever os dashboards antigos; integração com Meta Ads e CRM.

### Engenharia

**Arquitetura.** Sem mudança: portal estático no GitHub Pages (Tailwind por CLI), 3 dashboards HTML antigos abertos num iframe, e a versão Java (Spring Boot + JSF) não publicada.

**O que o teste achou e foi corrigido.**
- Portal: o seletor "Troca Rápida" não tinha nome (violação crítica); o título do visualizador pulava de h1 para h3; o seletor tirava o contorno de foco; o hover do "Tentar de novo" ficava com 4,46:1.
- Analytics v2: os 4 sliders do simulador não tinham rótulo (crítica) e 14 textos `text-slate-500` ficavam abaixo de 4,5:1.
- Analytics v1: selo com contraste baixo.
- Kanban: filtros inativos com opacidade 0,5 (até 2,58:1) e colunas roláveis sem acesso por teclado.

**Riscos.** Os dashboards dependem de CDN (#17); o teste deles precisa de rede no CI.

**Qualidade.** `tests/acessibilidade.spec.mjs` com `@axe-core/playwright` 4.10.2: 6 testes. Portal sem nenhuma violação; dashboards sem violação séria ou crítica.

**Definição de pronto.** `npm run links` e `npx playwright test` locais; CI verde (`mvn -B verify` e portal); `MVP.md` com uma tabela só; README atualizado.

### Plano de ação

**Este ciclo executa.** #12 e #13, as correções acima e a reorganização do `MVP.md`.

**Vira issue.** #16 (violações moderadas) e #17 (Play CDN). Seguem com o responsável: #9 e #10.
