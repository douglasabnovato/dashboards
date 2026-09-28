// Cartão do Kanban tático (código no padrão A.1.a, BU, etapa e dia opcional)
package com.learntech.dashboards.core.kanban;

import java.util.Objects;

public record Tarefa(String id, String titulo, String descricao, Projeto projeto, Etapa etapa, DiaSemana dia) {

    // exige id, título, projeto e etapa; descrição e dia são opcionais
    public Tarefa {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(titulo, "titulo");
        Objects.requireNonNull(projeto, "projeto");
        Objects.requireNonNull(etapa, "etapa");
        if (id.isBlank() || titulo.isBlank()) {
            throw new IllegalArgumentException("id e titulo são obrigatórios");
        }
    }

    // cópia da tarefa em outra etapa
    public Tarefa naEtapa(Etapa nova) {
        return new Tarefa(id, titulo, descricao, projeto, nova, dia);
    }
}
// fim de Tarefa.java
