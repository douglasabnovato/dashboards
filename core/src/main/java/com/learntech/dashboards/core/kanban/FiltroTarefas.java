// Filtro duplo do quadro: BU e dia (todos, sem dia definido ou um dia específico)
package com.learntech.dashboards.core.kanban;

import java.util.List;
import java.util.Optional;

public record FiltroTarefas(Optional<Projeto> projeto, FiltroDia dia) {

    /** Opções do filtro de dia. */
    public enum FiltroDia { TODOS, SEM_DIA, SEG, TER, QUA, QUI, SEX }

    // filtro que deixa passar tudo
    public static FiltroTarefas todos() {
        return new FiltroTarefas(Optional.empty(), FiltroDia.TODOS);
    }

    // verifica se a tarefa atende aos dois filtros
    public boolean aceita(Tarefa t) {
        if (projeto.isPresent() && t.projeto() != projeto.get()) {
            return false;
        }
        return switch (dia) {
            case TODOS -> true;
            case SEM_DIA -> t.dia() == null;
            default -> t.dia() != null && t.dia().name().equals(dia.name());
        };
    }

    // aplica o filtro a uma lista
    public List<Tarefa> aplicar(List<Tarefa> tarefas) {
        return tarefas.stream().filter(this::aceita).toList();
    }
}
// fim de FiltroTarefas.java
