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
