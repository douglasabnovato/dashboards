// Casos de uso do Kanban: consultar com filtros, mover respeitando o WIP e restaurar o quadro
package com.learntech.dashboards.api.kanban;

import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.FiltroTarefas;
import com.learntech.dashboards.core.kanban.Quadro;
import com.learntech.dashboards.core.kanban.RegrasKanban;
import com.learntech.dashboards.core.kanban.Tarefa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class KanbanService {

    private final TarefaRepository repositorio;
    private final String semana;

    // recebe o repositório e o rótulo da semana configurado
    public KanbanService(TarefaRepository repositorio, @Value("${dashboards.kanban.semana}") String semana) {
        this.repositorio = repositorio;
        this.semana = semana;
    }

    // quadro filtrado; o contador de WIP considera o quadro inteiro, como no original
    @Transactional(readOnly = true)
    public Quadro quadro(FiltroTarefas filtro) {
        List<Tarefa> todas = repositorio.findAllByOrderByOrdemAsc().stream().map(TarefaEntity::paraDominio).toList();
        return new Quadro(semana, RegrasKanban.WIP_LIMIT, RegrasKanban.emAndamento(todas), filtro.aplicar(todas));
    }

    // move a tarefa; lança WipExcedidoException (409) ou NoSuchElementException (404)
    @Transactional
    public Tarefa mover(String id, Etapa destino) {
        List<TarefaEntity> entidades = repositorio.findAllParaAtualizar();
        List<Tarefa> todas = entidades.stream().map(TarefaEntity::paraDominio).toList();
        Tarefa movida = RegrasKanban.mover(todas, id, destino);
        TarefaEntity alvo = entidades.stream().filter(e -> e.getId().equals(id)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Tarefa não encontrada: " + id));
        alvo.setEtapa(movida.etapa());
        return movida;
    }

    // devolve todas as tarefas para a etapa inicial do quadro
    @Transactional
    public void restaurar() {
        repositorio.findAllParaAtualizar().forEach(TarefaEntity::restaurar);
    }
}
// fim de KanbanService.java
