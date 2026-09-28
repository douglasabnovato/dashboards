// Endpoints do Kanban; todos exigem a chave X-Api-Key (ver ChaveApiFilter)
package com.learntech.dashboards.api.kanban;

import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.FiltroTarefas;
import com.learntech.dashboards.core.kanban.Projeto;
import com.learntech.dashboards.core.kanban.Quadro;
import com.learntech.dashboards.core.kanban.Tarefa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/kanban")
public class KanbanController {

    /** Corpo do PATCH: nova etapa. */
    public record Movimento(Etapa etapa) {
    }

    private final KanbanService servico;

    // recebe o serviço por injeção
    public KanbanController(KanbanService servico) {
        this.servico = servico;
    }

    // GET /api/kanban?projeto=BYTECLASS&dia=SEG|SEM_DIA|TODOS
    @GetMapping
    public Quadro quadro(@RequestParam(required = false) Projeto projeto,
                         @RequestParam(defaultValue = "TODOS") FiltroTarefas.FiltroDia dia) {
        return servico.quadro(new FiltroTarefas(Optional.ofNullable(projeto), dia));
    }

    // PATCH /api/kanban/tarefas/{id} {"etapa":"DOING"}: 200, 404 ou 409 (WIP)
    @PatchMapping("/tarefas/{id}")
    public Tarefa mover(@PathVariable String id, @RequestBody Movimento movimento) {
        Objects.requireNonNull(movimento.etapa(), "etapa é obrigatória");
        return servico.mover(id, movimento.etapa());
    }

    // POST /api/kanban/reset: restaura o quadro inicial (204)
    @PostMapping("/reset")
    public ResponseEntity<Void> restaurar() {
        servico.restaurar();
        return ResponseEntity.noContent().build();
    }
}
// fim de KanbanController.java
