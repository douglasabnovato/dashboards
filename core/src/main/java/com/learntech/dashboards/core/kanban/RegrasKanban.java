// Regra de Ouro do quadro: no máximo WIP_LIMIT tarefa(s) em Doing, contando todas as BUs
package com.learntech.dashboards.core.kanban;

import java.util.List;
import java.util.NoSuchElementException;

public final class RegrasKanban {

    public static final int WIP_LIMIT = 1;

    // classe utilitária, não deve ser instanciada
    private RegrasKanban() {
    }

    // devolve a tarefa movida; lança WipExcedidoException se Doing já estiver cheio (limite global, não por filtro)
    public static Tarefa mover(List<Tarefa> todas, String id, Etapa destino) {
        Tarefa alvo = todas.stream().filter(t -> t.id().equals(id)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Tarefa não encontrada: " + id));
        if (destino == Etapa.DOING && alvo.etapa() != Etapa.DOING) {
            List<Tarefa> ativas = todas.stream().filter(t -> t.etapa() == Etapa.DOING && !t.id().equals(id)).toList();
            if (ativas.size() >= WIP_LIMIT) {
                throw new WipExcedidoException(ativas.get(0).id());
            }
        }
        return alvo.naEtapa(destino);
    }

    // quantidade de tarefas em Doing em todo o quadro
    public static long emAndamento(List<Tarefa> todas) {
        return todas.stream().filter(t -> t.etapa() == Etapa.DOING).count();
    }
}
// fim de RegrasKanban.java
