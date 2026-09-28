// Erro de regra: mover a tarefa ultrapassaria o limite de WIP da coluna Doing
package com.learntech.dashboards.core.kanban;

public class WipExcedidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String tarefaAtiva;

    // informa qual tarefa já ocupa a coluna Doing
    public WipExcedidoException(String tarefaAtiva) {
        super("WIP Limit atingido: a tarefa " + tarefaAtiva + " já está em Doing. Conclua ou devolva antes de puxar outra.");
        this.tarefaAtiva = tarefaAtiva;
    }

    // id da tarefa que está em Doing
    public String tarefaAtiva() { return tarefaAtiva; }
}
// fim de WipExcedidoException.java
