-- Estrutura do banco: histórico mensal de Meta Ads e tarefas do Kanban tático
CREATE TABLE metrica_mensal (
    id          INTEGER      PRIMARY KEY,
    ano_mes     VARCHAR(7)   NOT NULL UNIQUE,
    rotulo      VARCHAR(10)  NOT NULL,
    gasto       NUMERIC(12,2) NOT NULL CHECK (gasto >= 0),
    cliques     INTEGER      NOT NULL CHECK (cliques >= 0),
    conversas   INTEGER      NOT NULL CHECK (conversas >= 0),
    cpc         NUMERIC(8,2) NOT NULL CHECK (cpc >= 0),
    cpa         NUMERIC(8,2) NOT NULL CHECK (cpa >= 0),
    fase        SMALLINT     NOT NULL CHECK (fase BETWEEN 1 AND 3)
);

CREATE TABLE tarefa (
    id            VARCHAR(16)  PRIMARY KEY,
    titulo        VARCHAR(120) NOT NULL,
    descricao     VARCHAR(500),
    projeto       VARCHAR(20)  NOT NULL CHECK (projeto IN ('MEDTREM','BYTECLASS','VOLTA_EXPRESS','ECOSSISTEMA')),
    etapa         VARCHAR(12)  NOT NULL CHECK (etapa IN ('BACKLOG','TODO','DOING','QA','CONCLUIDO')),
    etapa_inicial VARCHAR(12)  NOT NULL CHECK (etapa_inicial IN ('BACKLOG','TODO','DOING','QA','CONCLUIDO')),
    dia           VARCHAR(3)   CHECK (dia IN ('SEG','TER','QUA','QUI','SEX')),
    ordem         INTEGER      NOT NULL,
    versao        BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX idx_tarefa_etapa ON tarefa (etapa);
-- fim de V1__schema.sql
