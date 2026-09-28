// Colunas do quadro Kanban, na ordem do fluxo
package com.learntech.dashboards.core.kanban;

public enum Etapa {
    BACKLOG("Backlog Geral"),
    TODO("To-Do (Semana)"),
    DOING("Doing"),
    QA("QA / Testes"),
    CONCLUIDO("Concluído");

    private final String rotulo;

    // guarda o título da coluna
    Etapa(String rotulo) { this.rotulo = rotulo; }

    // título exibido na coluna
    public String rotulo() { return rotulo; }

    // etapa anterior no fluxo (a própria, se for a primeira)
    public Etapa anterior() { return ordinal() == 0 ? this : values()[ordinal() - 1]; }

    // próxima etapa no fluxo (a própria, se for a última)
    public Etapa proxima() { return ordinal() == values().length - 1 ? this : values()[ordinal() + 1]; }
}
// fim de Etapa.java
