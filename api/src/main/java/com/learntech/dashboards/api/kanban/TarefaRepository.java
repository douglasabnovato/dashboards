// Acesso às tarefas; a leitura com bloqueio serializa movimentos para garantir o WIP Limit
package com.learntech.dashboards.api.kanban;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TarefaRepository extends JpaRepository<TarefaEntity, String> {

    // todas as tarefas na ordem do quadro original
    List<TarefaEntity> findAllByOrderByOrdemAsc();

    // todas as tarefas com bloqueio de escrita (usada dentro da transação de movimento)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from TarefaEntity t order by t.ordem")
    List<TarefaEntity> findAllParaAtualizar();
}
// fim de TarefaRepository.java
