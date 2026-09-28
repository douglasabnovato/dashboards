// Testes do WIP Limit global e do filtro duplo do Kanban
package com.learntech.dashboards.core;

import com.learntech.dashboards.core.kanban.DiaSemana;
import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.FiltroTarefas;
import com.learntech.dashboards.core.kanban.Projeto;
import com.learntech.dashboards.core.kanban.RegrasKanban;
import com.learntech.dashboards.core.kanban.Tarefa;
import com.learntech.dashboards.core.kanban.WipExcedidoException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegrasKanbanTest {

    private final List<Tarefa> quadro = List.of(
            new Tarefa("a1a", "A.1.a - Mapeamento", "Reunião de finanças", Projeto.MEDTREM, Etapa.TODO, DiaSemana.SEG),
            new Tarefa("b3a", "B.3.a - Des. Plataforma", "PRs", Projeto.BYTECLASS, Etapa.DOING, DiaSemana.SEG),
            new Tarefa("d2c", "D.2.c - Famosos do Futuro", null, Projeto.ECOSSISTEMA, Etapa.TODO, null));

    // Doing já tem b3a: puxar a1a viola o WIP=1
    @Test
    void bloqueiaSegundaTarefaEmDoing() {
        WipExcedidoException e = assertThrows(WipExcedidoException.class,
                () -> RegrasKanban.mover(quadro, "a1a", Etapa.DOING));
        assertEquals("b3a", e.tarefaAtiva());
    }

    // mover a própria tarefa ativa ou para outras colunas é permitido
    @Test
    void permiteMovimentosQueNaoExcedem() {
        assertEquals(Etapa.QA, RegrasKanban.mover(quadro, "b3a", Etapa.QA).etapa());
        assertEquals(Etapa.DOING, RegrasKanban.mover(quadro, "b3a", Etapa.DOING).etapa());
        assertEquals(Etapa.BACKLOG, RegrasKanban.mover(quadro, "a1a", Etapa.BACKLOG).etapa());
        assertEquals(1, RegrasKanban.emAndamento(quadro));
    }

    // tarefa inexistente
    @Test
    void tarefaInexistente() {
        assertThrows(NoSuchElementException.class, () -> RegrasKanban.mover(quadro, "zzz", Etapa.QA));
    }

    // filtro duplo: BU + dia, e "sem dia definido"
    @Test
    void filtroDuplo() {
        assertEquals(2, new FiltroTarefas(Optional.empty(), FiltroTarefas.FiltroDia.SEG).aplicar(quadro).size());
        assertEquals(1, new FiltroTarefas(Optional.of(Projeto.MEDTREM), FiltroTarefas.FiltroDia.SEG)
                .aplicar(quadro).size());
        assertEquals("d2c", new FiltroTarefas(Optional.empty(), FiltroTarefas.FiltroDia.SEM_DIA)
                .aplicar(quadro).get(0).id());
        assertEquals(3, FiltroTarefas.todos().aplicar(quadro).size());
    }

    // navegação entre colunas nas pontas
    @Test
    void anteriorEProxima() {
        assertEquals(Etapa.BACKLOG, Etapa.BACKLOG.anterior());
        assertEquals(Etapa.CONCLUIDO, Etapa.CONCLUIDO.proxima());
        assertEquals(Etapa.DOING, Etapa.TODO.proxima());
    }
}
// fim de RegrasKanbanTest.java
