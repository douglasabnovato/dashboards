// Business Units do ecossistema learnTECH (A a D)
package com.learntech.dashboards.core.kanban;

public enum Projeto {
    MEDTREM("A", "MedTrem", "Saúde/ERP", "pi pi-heart"),
    BYTECLASS("B", "ByteClass", "Educação", "pi pi-book"),
    VOLTA_EXPRESS("C", "Volta Express", "Logística", "pi pi-truck"),
    ECOSSISTEMA("D", "Ecossistema", "Transversal/Eventos", "pi pi-globe");

    private final String letra;
    private final String nome;
    private final String area;
    private final String icone;

    // guarda os dados de exibição da BU
    Projeto(String letra, String nome, String area, String icone) {
        this.letra = letra;
        this.nome = nome;
        this.area = area;
        this.icone = icone;
    }

    // letra do código da tarefa (A, B, C, D)
    public String letra() { return letra; }

    // nome da BU
    public String nome() { return nome; }

    // área de atuação
    public String area() { return area; }

    // ícone PrimeIcons
    public String icone() { return icone; }
}
// fim de Projeto.java
