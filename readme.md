> **Projeto de estudo (N1).** Estado atual em [docs/MVP.md](docs/MVP.md); avaliação antiga (30/10/2026) em [docs/AVALIACAO.md](docs/AVALIACAO.md).

# learnTECH OS — Growth Hub (Java)

Sistema de gestão tática, análise de performance e simulador de tráfego pago do ecossistema **learnTECH**, com foco na BU **ByteClass**. Esta versão 2.0 reescreve a suíte HTML original em **Java**: API em Spring Boot e interface em JSF/PrimeFaces, usando os mesmos dados, fórmulas, presets e regras.

> **Estado em 16/11/2026:** O GitHub Pages publica só o portal estático (`index.html`), no modo legado (branch `main`, pasta `/`). A versão Java não está publicada: os endereços do Render abaixo são os esperados, se a publicação for feita. Portal: `npm ci`, `npm run css`, `npx playwright install chromium` e `npm test`, com Node 22.12 ou mais novo (porta 4173; outra porta com `PORTA_PORTAL=4791 npm test` no Bash ou `$env:PORTA_PORTAL=4791; npm test` no PowerShell). Critérios em [docs/MVP.md](docs/MVP.md).

## Estado e próximos passos (ciclo de 16/11/2026)

**Estado:** Estudo (N1), 9 de 10 critérios aplicáveis atendidos; **Finalizado: sim**. O critério 9 fica "Parcial" até o responsável ligar os alertas do Dependabot (#23). A `main` exige PR e os jobs `build` e `portal` do CI verdes (ruleset `principal-protegida`). Detalhes em [`docs/MVP.md`](docs/MVP.md).

**No ar:** o portal em https://douglasabnovato.github.io/dashboards/ respondeu 200 em 16/11/2026. A versão Java não está publicada; é estudo local até a decisão da #10.

**O que foi feito neste ciclo** (PR feature → `developer/sprint-2026-11-19`; a `main` espera a revisão do responsável)
- Analytics v1 com região `main` e títulos em ordem nos 3 dashboards; o axe passou a barrar também as violações moderadas (#16).
- O teste do portal "em claro e escuro" repetia a mesma tela, porque o portal não tem tema escuro; agora roda em 1280 px e 375 px.
- `@axe-core/playwright` 4.14.0 e `@playwright/test` 1.65.1; porta do portal por `PORTA_PORTAL`.
- Critério 9 corrigido para "Parcial" (Dependabot desligado); `docs/DEPLOY.md` com o estado real.

**Ciclos anteriores:** números ilustrativos e ruleset da `main` (PR #22, 10/11/2026); axe e teclado no CI (PR #18, 07/11/2026); carregando e erro no visualizador (03/11/2026). Todos já estão na `main`.

**Próximos passos**
- #23 Ligar os alertas do Dependabot (responsável).
- #10 Decidir a publicação da versão Java no Render (responsável).
- #11 Padronizar as fórmulas de ROAS, CPA e CAC.
- #17 Tirar o Tailwind Play CDN dos dashboards antigos.
- #24 Receber atualizações semanais de dependências e actions.
- #25 Gerar o CSS do portal com o Tailwind 4.
- #26 Estabilizar o teste do erro com "Tentar de novo".

**Onde está a documentação**
- Estado dos critérios: [`docs/MVP.md`](docs/MVP.md)
- Plano do ciclo, com as fontes da pesquisa: [`docs/PLANO-DE-ACAO.md`](docs/PLANO-DE-ACAO.md)
- Hospedagem (Pages e Render): [`docs/DEPLOY.md`](docs/DEPLOY.md)
- Arquitetura: [`docs/ARQUITETURA.md`](docs/ARQUITETURA.md)

## Em produção

- Portal (GitHub Pages): https://douglasabnovato.github.io/dashboards/
- Versão Java, **não publicada** (404 em 06/11/2026; #10): web `learntech-dashboards-web.onrender.com` e API `learntech-dashboards-api.onrender.com`, se a publicação for feita.
- Hospedagem gratuita: Render (2 serviços Docker, `render.yaml`) + Neon (PostgreSQL).
- Passo a passo, ordem segura e checagens: [docs/DEPLOY.md](docs/DEPLOY.md).

## O ciclo de gestão

```text
 Meta Ads / CRM ──► 1. ANALISAR (Analytics)   KPIs: CPA, CPC, conversas; filtro por fase
                    2. DECIDIR  (Simulador)   presets, alavancas, termômetro de ROAS, guia
                    3. EXECUTAR (Kanban)      cartão com código (ex.: B.1.d), dia da semana, WIP = 1
```

## Módulos

| Módulo | Stack | Papel |
|---|---|---|
| `core/` | Java 21 puro | Regras: fases, KPIs ponderados, simulador de funil, faixas de ROAS, WIP Limit, filtros do Kanban |
| `api/` | Spring Boot 3.5, Spring Data JPA, Flyway, H2 (local) / PostgreSQL (produção) | REST em `/api/*`, dados originais carregados por migração |
| `web/` | Spring Boot 3.5, JoinFaces 5.5, PrimeFaces 15, Spring Security | Páginas Início, Analytics, Simulador, Guia e Kanban (com login) |

## Telas

- **Início**: cartões de acesso aos módulos (substitui o `index.html` com iframe).
- **Analytics**: histórico Out/25 a Jul/26 com filtro por fase, KPIs (investimento, conversas, custo por conversa, CPC, CTR do relatório), gráfico de gasto × conversas, gráfico de CPC × custo por conversa, tabela mensal e diagnóstico.
- **Simulador**: presets Retomada Inteligente, High Ticket Bootcamp, Alerta Baixa Conversão e Baseline v1; alavancas de orçamento, CPA, conversão e ticket; projeção mensal; termômetro de ROAS; diagnóstico.
- **Guia**: números de cada fase calculados a partir dos dados, fórmulas do simulador e matriz de decisão por ROAS.
- **Kanban**: 37 cartões da semana 27 a 31/07/2026 nas BUs MedTrem, ByteClass, Volta Express e Ecossistema; filtro por BU e por dia; colunas Backlog, To-Do, Doing, QA e Concluído; **WIP Limit de 1 tarefa em Doing, garantido pela API**; restaurar quadro.

## API

| Método e rota | Descrição | Acesso |
|---|---|---|
| `GET /api/metricas?fase=1..3` | Painel: KPIs, meses e análise (sem `fase` = todas) | público |
| `GET /api/fases` | Fases com rótulo e período | público |
| `GET /api/simulador/presets` | Cenários prontos | público |
| `GET /api/simulador/faixas` | Matriz de decisão por ROAS | público |
| `POST /api/simulador` | Projeção para `{orcamentoDiario, cpa, conversaoPercentual, ticketMedio}` | público |
| `GET /api/kanban?projeto=BYTECLASS&dia=SEG` | Quadro filtrado (`dia` aceita `TODOS`, `SEM_DIA`, `SEG`…`SEX`) | `X-Api-Key` |
| `PATCH /api/kanban/tarefas/{id}` | `{"etapa":"DOING"}`; 409 se o WIP estourar | `X-Api-Key` |
| `POST /api/kanban/reset` | Volta o quadro ao estado inicial | `X-Api-Key` |
| `GET /actuator/health` | Saúde | público |

Erros seguem Problem Details (RFC 9457), sem stack trace.

## Como executar

Pré-requisitos: **JDK 21** e **Maven 3.9** (ou o IntelliJ, que traz o Maven embutido).

```bash
mvn verify                                   # compila e roda todos os testes

# terminal 1 — API em http://localhost:8081 (H2 em ./data, criado na primeira execução)
DASHBOARDS_API_KEY=troque-esta-chave mvn -pl api -am spring-boot:run

# terminal 2 — Web em http://localhost:8080
DASHBOARDS_API_KEY=troque-esta-chave KANBAN_SENHA=sua-senha mvn -pl web -am spring-boot:run
```

No Windows (PowerShell), defina as variáveis antes: `$env:DASHBOARDS_API_KEY="troque-esta-chave"`.

O login do Kanban usa `KANBAN_USUARIO` (padrão `admin`) e `KANBAN_SENHA`. Sem `KANBAN_SENHA`, o Kanban fica bloqueado. Sem `DASHBOARDS_API_KEY` na API, as rotas do Kanban respondem 401.

## Testes

- `core`: KPIs contra o relatório (1.007 conversas, CPA R$ 9,27, CPC R$ 0,94), totais de cada fase, fórmulas e presets do simulador, limites das faixas de ROAS, WIP Limit e filtros.
- `api`: integração com H2 + Flyway (dados originais, filtros, 400/401/404/409).
- `web`: formatação pt-BR, JSON dos gráficos, beans com API simulada, cliente HTTP contra servidor real (PATCH e 409) e fumaça das páginas.

## Publicar (gratuito)

Resumo (detalhes em [docs/DEPLOY.md](docs/DEPLOY.md)):

1. No **Neon**, crie o banco `dashboards` (um projeto para o portfólio, um banco por app) e copie host, usuário e senha.
2. No **Render**, **New → Blueprint** com este repositório; ele lê o `render.yaml` e cria `learntech-dashboards-api` e `learntech-dashboards-web` (plano free, Docker, deploy a cada commit).
3. Informe na API: `SPRING_DATASOURCE_URL` (`jdbc:postgresql://<host>/dashboards?sslmode=require`), `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`. A `DASHBOARDS_API_KEY` é gerada e repassada à Web.
4. Informe na Web: `KANBAN_SENHA`. A `DASHBOARDS_API_URL` já vem fixada no `render.yaml`.

No plano free, os serviços "dormem" após 15 min sem acesso; a primeira visita pode levar cerca de 1 minuto (a web mostra aviso e botão "Tentar de novo"). Os Dockerfiles limitam a memória da JVM para caber nos 512 MB do plano.

## Como adicionar dados

- **Novo mês de Meta Ads**: crie `api/src/main/resources/db/migration/V3__<descricao>.sql` com o `INSERT` em `metrica_mensal` (nunca edite migrações já aplicadas).
- **Nova semana do Kanban**: nova migração com os cartões e ajuste `KANBAN_SEMANA`.

## Estrutura

```text
dashboards/
├── core/   domínio (metricas, simulador, kanban) + testes
├── api/    Spring Boot REST + Flyway (V1 estrutura, V2 dados originais) + testes
├── web/    JoinFaces/PrimeFaces (META-INF/resources/*.xhtml) + testes
├── docs/   MVP, PLANO-DE-ACAO, DEPLOY, ARQUITETURA, ANALISE
├── index.html, dashboard/, kanban/   portal estático (GitHub Pages) e dashboards HTML
├── tests/  testes Playwright do portal, com axe
├── .github/workflows/ci.yml   CI: jobs build (mvn -B verify) e portal (Playwright)
├── render.yaml, .env.example
└── pom.xml
```

## Autor

Douglas A. B. Novato — projeto de portfólio e ferramenta interna do ecossistema learnTECH.
