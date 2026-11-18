# Deploy · learnTECH OS / Growth Hub (Java)

Plano de ação para publicar a versão Java (API Spring Boot + Web JSF/PrimeFaces) em hospedagem gratuita, sem tirar do ar a versão HTML antes da hora.

> **Estado em 16/11/2026.** O portal estático está no ar pelo GitHub Pages (seção 0). A versão Java não está publicada: as Etapas 1 e 2 estão concluídas (o CI roda o `mvn -B verify` verde, run [43799414312](https://github.com/douglasabnovato/dashboards/actions/runs/43799414312) da `main`), e as Etapas 3 a 7 esperam a decisão do responsável na #10.

## 0. Portal estático no GitHub Pages (no ar)

| Item | Valor | Conferido |
|---|---|---|
| Endereço | https://douglasabnovato.github.io/dashboards/ | `curl -s -o /dev/null -w "%{http_code}"`, 16/11/2026: 200 |
| Modo | Legado: publica a branch `main`, pasta `/` (raiz), sem workflow | `gh api repos/douglasabnovato/dashboards/pages`, 16/11/2026: `build_type: legacy`, `status: built` |
| O que vai ao ar | `index.html`, `assets/`, `dashboard/` e `kanban/`. Os arquivos Java ficam no repositório, mas o Pages só serve arquivos estáticos | — |
| Quando atualiza | A cada merge na `main` (que exige PR e os jobs `build` e `portal` verdes) | — |

Para conferir depois de um merge na `main`:
1. Abrir **Actions** e esperar o workflow `pages build and deployment` ficar verde.
2. Rodar `curl -s https://douglasabnovato.github.io/dashboards/ | grep -o "<title>[^<]*"` e comparar com o `<title>` do `index.html` da `main`.

Não há workflow próprio de Pages: o modo legado já publica a `main` sem passo manual. Trocar para "GitHub Actions" em **Settings → Pages** é configuração do responsável e não é necessário hoje.

### Atualizações de dependências (Dependabot)

O `.github/dependabot.yml` pede, toda semana, um PR agrupado para o npm do portal, outro para o Maven (core, api e web) e outro para as actions do CI. Versão maior do npm e do Maven fica de fora: é migração, com issue própria (como a #25). Versão maior das actions vem num PR à parte.

- Os PRs vão para a `main` e rodam os jobs `build` e `portal` do CI.
- **Quem mescla é o responsável**, com o CI verde (regra do fluxo Git do portfólio: o merge de PR do Dependabot pelo agente é negado).
- Para conferir: `gh pr list -R douglasabnovato/dashboards --author "app/dependabot"`.

## 1. Desafio

Colocar no ar, sem custo, dois serviços Java 21 (API e Web) e um banco PostgreSQL, cabendo nos **512 MB de RAM** do Render free, protegendo o Kanban (dados internos) e só apagando os HTML antigos (`index.html`, `dashboard/`, `kanban/`) quando a versão nova estiver respondendo.

## 2. Conteúdo

### Decisão de hospedagem

| Opção | Resultado |
|---|---|
| **Render free, 2 serviços Docker + Neon PostgreSQL free (escolhida)** | Roda Java sem custo; o `render.yaml` cria tudo de uma vez; Neon guarda o Kanban entre deploys |
| GitHub Pages | Só serve arquivos estáticos; não executa Spring Boot/JSF |
| Um único serviço Render (API + Web juntas) | Economizaria horas, mas muda a arquitetura (ADR-01); fica como decisão pendente |
| Railway / Fly.io | Sem plano gratuito estável |
| Banco H2 no disco do Render | O disco é apagado a cada deploy; o Kanban voltaria ao início |

### Por que só pode sair do ar o HTML depois

O repositório `dashboards` publica hoje os HTML da raiz (se o GitHub Pages estiver ativo). Se você rodar o `git rm` antes de o Render estar no ar, o endereço antigo cai e o novo ainda não existe. Por isso o `git rm` é a **Etapa 6**, não a 3.

### O que foi ajustado para produção

| Mudança | Arquivo | Por quê |
|---|---|---|
| JVM limitada: `-XX:MaxRAMPercentage=55 -XX:+UseSerialGC -XX:MaxMetaspaceSize=192m -XX:ReservedCodeCacheSize=48m -XX:TieredStopAtLevel=1 -Xss512k -XX:+ExitOnOutOfMemoryError` | `api/Dockerfile`, `web/Dockerfile` | Heap de ~280 MB + metaspace + code cache + threads cabem em 512 MB; o antigo 75% de heap somado ao restante passava do limite e o Render mataria o processo. Sem compilador C2 a subida é mais rápida e usa menos memória |
| `autoDeployTrigger: commit` nos dois serviços | `render.yaml` | Cada push na `main` publica sozinho |
| `DASHBOARDS_API_URL` fixa em `https://learntech-dashboards-api.onrender.com` e `KANBAN_USUARIO` = `admin` | `render.yaml` | Menos variáveis para digitar; só segredos ficam com `sync: false` |
| Pool do banco em 3 conexões e Tomcat com 20 threads (por variável de ambiente) | `render.yaml` | Menos memória e respeita o limite de conexões do Neon free |
| Seção "Em produção" e resumo de publicação | `readme.md` | URL esperada e link para este guia |

Nenhum código Java foi alterado.

### Limitações do plano gratuito

- Cada serviço dorme após 15 min sem acesso; a primeira visita leva cerca de 1 min (a Web acorda e depois acorda a API: pode chegar a 2 min). A Web já mostra aviso e botão "Tentar de novo".
- As 750 horas/mês são da conta Render inteira: dois serviços acordados o mês todo gastariam 1.440 h. Como dormem sem uso, na prática cabe, mas o portfólio todo divide essa cota.
- O build Docker baixa as dependências do Maven a cada deploy (5 a 10 min).
- Neon free: o banco suspende sem uso e acorda na primeira conexão (1 a 2 s); armazenamento de 0,5 GB por projeto, compartilhado entre os bancos do portfólio.

### Segurança e LGPD

- Segredos só no painel do Render: `SPRING_DATASOURCE_PASSWORD`, `DASHBOARDS_API_KEY` (gerada pelo Render) e `KANBAN_SENHA`. Nunca no `.env` versionado.
- `KANBAN_SENHA` forte (16+ caracteres): sem ela o Kanban fica bloqueado, com ela é a única barreira para os cartões internos (BUs MedTrem, ByteClass, Volta Express).
- As métricas de Meta Ads ficam públicas (decisão ADR-07). Se não quiser expor investimento e custo por conversa, avise antes de publicar.
- Não há dados pessoais de terceiros no banco.

## 3. Solução (passo a passo)

Branch: **main**.

### Etapa 1 · Validar o build (concluída no CI; rodar local é opcional)

Situação em 16/11/2026: o job `build` do CI roda `mvn -B verify` (core, api e web) e passa, com `Skipped: 0` (run [43799414312](https://github.com/douglasabnovato/dashboards/actions/runs/43799414312)). O registro abaixo, de 27/09/2026, fica como histórico. Para rodar na sua máquina, siga os passos.

Log resumido da última tentativa (27/09/2026, sandbox com JDK 21.0.10 e Maven 3.9.11):

```text
[FATAL] Non-resolvable parent POM for com.learntech:dashboards:2.0.0:
  org.springframework.boot:spring-boot-starter-parent:pom:3.5.14 ...
  from/to central (https://repo.maven.apache.org/maven2): status code: 403, Forbidden
```

- O Maven Central (e os espelhos repo1.maven.org e Google) está bloqueado pela política de rede da sandbox: não é erro do projeto.
- Além disso, os arquivos `.java` ficam fundo demais nas pastas (`api/src/main/java/com/learntech/dashboards/api/...`) para a ferramenta de cópia do computador, então nem o código foi compilado aqui. Só o core (17 testes) e 2 testes da web rodaram no ciclo anterior.

Faça no Git Bash:

1. `cd /c/ambiente-projeto/ser-mvp/dashboards`
2. `java -version` (precisa ser 21) e `mvn -v` (3.9.x). Se faltar, instale o **Temurin 21** (adoptium.net) e o **Maven 3.9** (maven.apache.org, descompacte e ponha `bin` no PATH), ou abra o projeto no IntelliJ, que traz o Maven.
3. `mvn -B verify`
4. Esperado: `BUILD SUCCESS` nos 4 projetos (dashboards, core, api, web), com os testes do core (17), da api (integração H2 + Flyway) e da web (5 classes).
5. Se falhar, copie as 40 linhas finais do terminal e mande no chat. Não siga para a Etapa 2.
6. Teste de fumaça, em dois terminais:
   - `DASHBOARDS_API_KEY=teste-local mvn -pl api -am spring-boot:run` → abrir `http://localhost:8081/actuator/health` (espera `{"status":"UP"}`) e `http://localhost:8081/api/metricas`.
   - `DASHBOARDS_API_KEY=teste-local KANBAN_SENHA=senha-local mvn -pl web -am spring-boot:run` → abrir `http://localhost:8080`, as 5 telas, login no Kanban, mover 2 cartões para Doing (o 2º deve ser recusado).
7. Opcional, para testar a memória como no Render (se tiver Docker Desktop): `docker build -f api/Dockerfile -t dash-api .` e `docker run --rm -m 512m -e DASHBOARDS_API_KEY=x -p 8081:8081 dash-api`; `docker stats` deve ficar abaixo de ~450 MB.

### Etapa 2 · Subir para o GitHub (concluída)

O código Java e o CI (`.github/workflows/ci.yml`, jobs `build` e `portal`) já estão na `main`. A `main` é protegida: toda mudança entra por PR, com o CI verde.

### Etapa 3 · Criar o banco no Neon

1. Entrar em **neon.tech** com a conta do GitHub.
2. Usar um único projeto para o portfólio (ex.: `portfolio`, região **AWS São Paulo** ou **US East**). Se ainda não existir, criar.
3. Em **Databases → New Database**: nome `dashboards` (um banco por app).
4. Em **Connect**, escolher o banco `dashboards` e anotar host (`ep-...neon.tech`), usuário e senha.
5. Montar a URL JDBC: `jdbc:postgresql://<host>/dashboards?sslmode=require` (sem usuário e senha na URL).
6. Não criar tabelas: o Flyway da API cria (V1) e carrega os dados originais (V2) no primeiro start.

### Etapa 4 · Criar os serviços no Render

1. Entrar em **render.com** com a conta do GitHub e autorizar o repositório `dashboards`.
2. **New → Blueprint**, escolher `douglasabnovato/dashboards`, branch `main`.
3. Preencher o que o Render pedir:
   - `learntech-dashboards-api`: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` (do Neon).
   - `learntech-dashboards-web`: `KANBAN_SENHA`.
4. **Apply**. O build Docker leva de 5 a 10 min por serviço.
5. Nos **Logs** da API, esperar `Successfully applied 2 migrations` e `Started DashboardsApiApplication`. Na Web, `Started DashboardsWebApplication`.
6. Se a URL da API não for `https://learntech-dashboards-api.onrender.com` (nome já usado), corrigir `DASHBOARDS_API_URL` na Web e no `render.yaml`.

### Etapa 5 · Conferir no ar

1. `https://learntech-dashboards-api.onrender.com/actuator/health` → `{"status":"UP"}`.
2. `https://learntech-dashboards-api.onrender.com/api/metricas` → JSON com 10 meses; `/api/kanban` sem chave → **401**.
3. `https://learntech-dashboards-web.onrender.com` → Início com os cartões; Analytics mostra 1.007 conversas e CPC R$ 0,94.
4. Simulador: trocar o preset muda a projeção e o termômetro de ROAS.
5. Kanban pede login; com `admin` + `KANBAN_SENHA` abre os 37 cartões; o 2º cartão em Doing é recusado (WIP = 1).
6. **Metrics** de cada serviço no Render: memória abaixo de ~450 MB depois de navegar pelas 5 telas. Se aparecer "Out of memory" / reinícios, mande o log.
7. Fazer um deploy manual (**Manual Deploy**) e confirmar que o Kanban manteve o estado (prova de que está no Neon, não no H2).

### Etapa 6 · Só agora: retirar a versão HTML

1. Conferir de novo o item 3 da Etapa 5.
2. `git rm index.html dashboard/2-analytics_dashboard_v1-14-08-2026.html dashboard/3-analytics_dashboard_v2-14-08-2026.html kanban/1-weekly_dashboard-31-07-2026.html`
3. `git commit -m "chore: remove a versão HTML substituída pela versão Java"` numa branch e abrir PR para a `main` (o push direto é bloqueado pelo ruleset)
4. Se o GitHub Pages estava ativo: **Settings → Pages → Unpublish site** (o repositório passa a ter só código Java).

### Etapa 7 · Fechar

1. Se a URL real da Web for diferente de `https://learntech-dashboards-web.onrender.com`, corrigir no `readme.md`, commit e push.
2. No GitHub, **About → Website**: colar a URL da Web.
