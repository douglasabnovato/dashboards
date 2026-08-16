# 🚀 LearnTECH OS — Growth Hub & Tactical Dashboard Suite

Sistema Integrado de Gestão Tática, Análise de Performance e Simulador de Tráfego Pago do ecossistema **learnTECH** (com foco na Business Unit **ByteClass** e suporte às demais BUs).

O projeto é estruturado como um **Portal Modular Desacoplado**, onde cada dashboard e quadro Kanban funciona como uma aplicação independente (`.html` autônomo), sendo centralizado e gerenciado através de uma aplicação de navegação raiz (`index.html`).

---

## 🔄 O Ciclo Operacional de Gestão (Dashboard → Tarefas)

```plaintext
 ┌───────────────────┐        📊 1. ANALISAR (Dashboard)
 │   Meta Ads / CRM  │        • Ler KPIs (CPA, CPC, Conversão)
 └─────────┬─────────┘        • Simular cenários no Analytics v2
           │
           ▼
 ┌───────────────────┐        💡 2. DECIDIR (Estratégia)
 │  Dashboard (v2)   │        • Identificar gargalos (ex: CPA subindo)
 └─────────┬─────────┘        • Definir ação corretiva
           │
           ▼
 ┌───────────────────┐        📋 3. EXECUTAR (Kanban)
 │  Kanban Semanal   │        • Criar card tático com código (ex: B.1.d)
 └───────────────────┘        • Alocar no dia da semana e respeitar WIP=1
```

---

## 🛠️ Tecnologias Utilizadas

Toda a suite foi construída focando em **desempenho, facilidade de implantação e zero dependência de servidor** (Client-side puro):

* **HTML5 Semantic & Modular Framework**
* **Tailwind CSS (via CDN)** — Interface *Dark Mode Pro* moderna e responsiva
* **Chart.js (via CDN)** — Gráficos interativos para análise de dados e evolução financeira
* **FontAwesome 6 (via CDN)** — Iconografia tática do ecossistema
* **JavaScript ES6+** — Lógica do simulador em tempo real, manipulação de estados no localStorage, movimentação de quadros e filtros dinâmicos

---

## 📁 Arquitetura do Repositório

```text
dashboards/
│
├── dashboard/                               # Módulos Analytics e Relatórios de Mídia
│   ├── 2-analytics_dashboard_v1-14-08-2026.html  # Baseline auditado (Meta Ads Out/25 - Jul/26)
│   └── 3-analytics_dashboard_v2-14-08-2026.html  # Dashboard v2 (Simulador + Guia Tático + ROAS)
│
├── kanban/                                  # Quadros Operacionais de Gestão Tática
│   └── 1-weekly_dashboard-31-07-2026.html         # Tactical Kanban OS (Filtros BU, Dias e WIP Limit)
│
├── index.html                               # Portal / Hub Central de Navegação
└── readme.md                                # Documentação do projeto
```

---

## 🧩 Detalhamento dos Módulos

### 📊 1. Pasta `dashboard/` (Analytics & Mídia Paga)

* **`2-analytics_dashboard_v1-14-08-2026.html` (v1.0 Baseline):**
  * **Objetivo:** Registrar com rigor auditado o histórico de tráfego pago da ByteClass (Meta Ads).
  * **Dados Consolidados:** R$ 9.335 investidos | 1.007 conversas no WhatsApp | CPA Médio R$ 9,27 | CPC R$ 0,94.
  * **Recursos:** Filtros por fase histórica (Aprendizado, Virada de Chave e Manutenção), tabela mensal completa e gráficos de investimento x resultado.

* **`3-analytics_dashboard_v2-14-08-2026.html` (v2.0 Integrado + Guia):**
  * **Objetivo:** Unir o simulador financeiro ao guia de leitura estratégica para tomada de decisão.
  * **Recursos:**
    * **Simulador de Funil:** Ajuste de orçamento diário, CPA esperado, taxa de conversão comercial do WhatsApp e Ticket Médio das formações.
    * **Presets Rápidos:** Botões de cenários (*Retomada Inteligente*, *High Ticket Bootcamp* e *Alerta de Baixa Conversão*).
    * **Termômetro de ROAS:** Diagnóstico financeiro em tempo real com orientações de leilão e escala.
    * **Guia Interativo:** Aba exclusiva com fórmulas matemáticas, interpretação das 3 fases e matriz estratégica.

---

### 📋 2. Pasta `kanban/` (Gestão Operacional)

* **`1-weekly_dashboard-31-07-2026.html` (Tactical Kanban OS):**
  * **Objetivo:** Gestão de capacidade e fluxo de trabalho semanal do ecossistema.
  * **Recursos:**
    * **Organização por Business Units (BUs):** `MedTrem` (Saúde/ERP), `ByteClass` (Educação), `Volta Express` (Logística) e `Ecossistema` (Transversal/Eventos).
    * **Filtro Duplo:** Seleção combinada por Projeto/BU e por Dia da Semana (Segunda a Sexta).
    * **Regra de Ouro (WIP Limit):** Limite estrito de **1 tarefa ativa na coluna *Doing*** com sistema de alertas visuais para evitar multitarefa e foco na conclusão.

---

### 🌐 3. Raiz (`index.html`)

* **Hub Central de Navegação:**
  * Apresenta uma Home com cartões informativos de todos os dashboards e quadros da suite.
  * **Visualizador Embutido (Viewport/Iframe):** Permite alternar entre qualquer relatório ou Kanban em 1 clique sem recarregar a página.
  * Botões de acesso rápido para abrir qualquer arquivo diretamente em uma nova aba do navegador.

---

## ➕ Como Adicionar Novos Dashboards ou Kanbans

Para manter a organização do projeto ao criar novos módulos:

1. **Salvar o arquivo na subpasta correta:**
   * Se for um dashboard/relatório analítico: salve dentro de `dashboard/` (ex: `dashboard/4-funnel_analytics_v3.html`).
   * Se for um quadro Kanban/tarefas: salve dentro de `kanban/` (ex: `kanban/2-sprint_kanban_aug2026.html`).

2. **Registrar o novo módulo no `index.html`:**
   * Abra o arquivo `index.html` na raiz.
   * Adicione o novo card na seção correspondente (Dashboards ou Kanbans).
   * Adicione a nova opção `<option>` no menu `<select id="quick-selector">` do visualizador para habilitar a troca rápida.

---

## ⚡ Como Executar o Projeto

Como a aplicação é 100% estática:

1. Clone ou baixe o repositório na sua máquina.
2. Dê dois cliques no arquivo `index.html` localizado na raiz.
3. A aplicação abrirá instantaneamente em qualquer navegador moderno (Chrome, Edge, Firefox, Safari).