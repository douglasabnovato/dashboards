# learnTECH OS — Growth Hub (Java)

Sistema de gestão tática, análise de performance e simulador de tráfego pago do ecossistema **learnTECH**, com foco na BU **ByteClass**. Esta versão 2.0 reescreve a suíte HTML original em **Java**: API em Spring Boot e interface em JSF/PrimeFaces, usando os mesmos dados, fórmulas, presets e regras.

## Em produção

- Web: `https://learntech-dashboards-web.onrender.com` (esperada) · API: `https://learntech-dashboards-api.onrender.com`
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
├── docs/   ANALISE, ARQUITETURA, PLANO-DE-ACAO
├── ci/     workflow do GitHub Actions (mover para .github/workflows/)
├── render.yaml, .env.example
└── pom.xml
```

## Autor

Douglas A. B. Novato — projeto de portfólio e ferramenta interna do ecossistema learnTECH.
