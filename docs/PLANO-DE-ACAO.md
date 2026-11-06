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
