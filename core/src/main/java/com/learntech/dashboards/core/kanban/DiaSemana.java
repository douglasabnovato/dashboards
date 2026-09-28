// Dias úteis em que uma tarefa pode ser alocada
package com.learntech.dashboards.core.kanban;

public enum DiaSemana {
    SEG("Segunda"), TER("Terça"), QUA("Quarta"), QUI("Quinta"), SEX("Sexta");

    private final String nome;

    // guarda o nome por extenso
    DiaSemana(String nome) { this.nome = nome; }

    // nome por extenso para filtros
    public String nome() { return nome; }
}
// fim de DiaSemana.java
