# Análise — learnTECH OS / Growth Hub

Ciclo: lote 2 · pedido: refazer em **stack Java** (frontend e backend) usando os dados originais · decisão sua: Spring Boot API + JSF/PrimeFaces · grupo da rubrica: fullstack.

## O que era o original

Quatro HTML estáticos (Tailwind, Chart.js e FontAwesome via CDN): `index.html` (hub com iframe), `dashboard/2-…v1` (histórico + simulador inicial), `dashboard/3-…v2` (histórico, simulador com presets, termômetro de ROAS e guia) e `kanban/1-…` (quadro com localStorage).

## Dados preservados

- 10 meses de Meta Ads (Out/25 a Jul/26): gasto, cliques, conversas, CPC e custo por conversa, fase 1 a 3.
- Textos de análise das fases, notas do especialista, faixas e recomendações do termômetro de ROAS.
- Presets Retomada Inteligente (50 / 8,0 / 5% / 800), High Ticket (60 / 12,0 / 4% / 1.800), Alerta (30 / 10,0 / 1,5% / 500) e o padrão do v1 (30 / 9,27 / 5% / 800), com as faixas dos controles.
- 37 cartões do Kanban, BUs, etapas, dias, semana 27 a 31/07/2026 e WIP = 1.

## Defeitos encontrados no original

| # | Defeito | Evidência | Tratamento |
|---|---|---|---|
| D1 | CPC médio calculado como média simples dos meses | Ao clicar "Todas as Fases" o card trocava de R$ 0,94 para R$ 1,07 | CPC ponderado = gasto ÷ cliques (R$ 0,94) |
| D2 | KPIs escritos à mão no HTML divergiam do cálculo | Guia: Fase 1 "R$ 11,11" (3.413 ÷ 307 = 11,12); Fase 2 "R$ 4.166" (soma dá 4.167) | Guia calcula os números a partir dos dados |
| D3 | Total "R$ 9.335" no README e no card | Soma dos meses do próprio relatório = R$ 9.336 | Mostra a soma dos dados; ver DECISÃO SUA |
| D4 | Três limiares diferentes para ROAS | Termômetro 1 / 2,5 / 4,5; nota do especialista 2; matriz 1-2 / 2,5-4 / >5 (com buracos) | Uma única tabela de faixas no core |
| D5 | WIP Limit podia ser furado com "OK" no `confirm` | `handleMove` permitia forçar | API recusa com 409 |
| D6 | Quadro só no localStorage de um navegador | Sem persistência compartilhada | Banco (H2/PostgreSQL) via API |
| D7 | Dados copiados entre v1 e v2 | Mesmo `rawData` em dois arquivos | Fonte única: migração Flyway V2 |
| D8 | Tailwind JIT e bibliotecas via CDN sem SRI | `<script src="https://cdn.tailwindcss.com">` | Tudo servido pelo próprio app; CSP restrita |
| D9 | Kanban exposto a qualquer visitante | Sem autenticação | Login na web e X-Api-Key na API |
| D10 | Nenhum teste | — | Testes em core, api e web |

## Evidências (depois)

- `core`: 17 testes executados com `javac` + um substituto mínimo do JUnit (o Maven Central está bloqueado neste ambiente); todos passaram. Mais 2 testes da web (`FormatosTest`) passaram do mesmo jeito.
- Migrações V1 e V2 aplicadas num PostgreSQL 16 local: 10 meses, R$ 9.336, 1.007 conversas, CPA 9,27, CPC 0,94; 37 tarefas (31 Backlog, 4 To-Do, 1 Doing, 1 QA).
- Revisão independente (subagente) do código Spring/JSF contra os fontes de Spring 6.2, Security 6.5, JoinFaces 5.5.15, PrimeFaces 15.0.16 e Mojarra 4.0.18: 4 problemas encontrados e corrigidos (PATCH com `HttpURLConnection`, desmarcar o filtro de dia, `process` do slider, pasta de dados no Docker).
- **Não executados aqui**: `mvn verify` (testes da api e da web), subir os serviços, axe e Lighthouse. Por isso C1, C2, C3, C4, C8 e C9 estão limitados pela regra de evidência.

## Rubrica v2 (grupo fullstack)

Aprovação: média ponderada ≥ 7,0 **e** C1 e C4 (eliminatórios) ≥ 5. Regras: nota sem evidência vale no máximo 6; C1 limitado a 7 para parte não executada de ponta a ponta; C9 ≥ 8 só com URL publicada e CI verde.

| # | Critério | Referência | Peso | Antes | Depois | Evidência | Justificativa |
|---|---|---|---|---|---|---|---|
| C1 | Núcleo de valor | MVP (Ries); SWEBOK Requirements | 16% | 6 | 7 | Core testado (17 testes): KPIs e fórmulas batem com o relatório; fluxo web não executado | C1 limitado a 7: ponta a ponta não executado |
| C2 | Estados e condições excepcionais | Nielsen; OWASP A10:2025 | 8% | 3 | 6 | Estados de API fora do ar, 400/401/404/409 codificados e testados só por escrito (api/web) | Sem evidência executada nas camadas api/web: máximo 6 |
| C3 | Acessibilidade | WCAG 2.2 AA (axe-core) | 7% | 3 | 6 | Labels, lang pt-BR, tabela junto dos gráficos, aria-live; sem axe (não roda sem build) | Sem medição automatizada: máximo 6 |
| C4 | Segurança e privacidade | OWASP Top 10:2025 / ASVS 5.0 N1 | 14% | 4 | 6 | Chave X-Api-Key em tempo constante e falha fechada; login no Kanban; CSP; sem execução | Sem teste executado da segurança: máximo 6 |
| C5 | Dados | 3FN / ACID / fonte única | 10% | 3 | 8 | Flyway V1+V2 aplicado no PostgreSQL 16 local: 10 meses, R$ 9.336, 1.007 conversas, CPA 9,27, CPC 0,94; 37 tarefas | Antes: dados duplicados em 2 HTML e KPIs fixos no HTML divergentes do cálculo |
| C6 | Testes | Pirâmide de testes; SWEBOK Testing | 9% | 0 | 7 | 17 testes do core + 2 da web executados (javac + shim JUnit); 6 da api e 11 da web escritos, não executados | Antes: nenhum teste |
| C7 | Qualidade de código | SOLID / camadas; SWEBOK Construction | 7% | 3 | 8 | core sem framework; api e web separados; contrato por records do core | Antes: v1 e v2 com código copiado |
| C8 | Desempenho | Complexidade; Core Web Vitals | 5% | 5 | 6 | Sem medição (sem build) | Antes: Tailwind JIT via CDN em produção |
| C9 | Operação | 12-Factor; DORA | 7% | 4 | 6 | Dockerfiles, render.yaml e CI escritos; nada publicado | C9 ≤ 7 sem URL e CI verde |
| C10 | Documentação | README como contrato | 5% | 7 | 8 | README: rodar, testar, API, publicar, adicionar dados | Antes: README bom, mas sem testes/deploy |
| C11 | Produto e evidência | Cagan (4 riscos); Torres | 7% | 6 | 7 | Corrige CPC médio e limites de ROAS divergentes; revisão por subagente com fontes oficiais | Antes: CPC exibido R$ 1,07 ao filtrar 'Todas' (média simples) contra R$ 0,94 do relatório |
| C12 | Sustentabilidade técnica | OWASP A03:2025; SWEBOK Maintenance | 5% | 4 | 6 | Spring Boot 3.5.14, JoinFaces 5.5.15, PrimeFaces 15.0.16 conferidos nos repositórios; build não executado | Dependências não resolvidas aqui (Maven Central bloqueado) |

**Média ponderada:** antes **3,98** (REPROVADO) → depois **6,76** (REPROVADO).


**Para aprovar (projeção):** com `mvn verify` verde (C2 e C6 → 8), fluxo ponta a ponta nas 5 telas (C1 → 8), axe sem violações (C3 → 8) e o teste de segurança passando (C4 → 8), a média sobe para cerca de 7,6. Com deploy no Render e CI verde, C9 pode chegar a 8.
