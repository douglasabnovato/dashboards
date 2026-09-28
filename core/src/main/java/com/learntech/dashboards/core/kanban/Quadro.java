// Estado do quadro Kanban devolvido pela API: semana, limite de WIP, tarefas ativas e cartões filtrados
package com.learntech.dashboards.core.kanban;

import java.util.List;

public record Quadro(String semana, int wipLimit, long emAndamento, List<Tarefa> tarefas) {

    // cartões de uma coluna, na ordem recebida
    public List<Tarefa> daEtapa(Etapa etapa) {
        return tarefas.stream().filter(t -> t.etapa() == etapa).toList();
    }
}
// fim de Quadro.java
