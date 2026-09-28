// Linha da tabela tarefa (cartão do Kanban), com etapa inicial para o reset e versão para concorrência
package com.learntech.dashboards.api.kanban;

import com.learntech.dashboards.core.kanban.DiaSemana;
import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.Projeto;
import com.learntech.dashboards.core.kanban.Tarefa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "tarefa")
public class TarefaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Projeto projeto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Etapa etapa;

    @Enumerated(EnumType.STRING)
    @Column(name = "etapa_inicial", nullable = false)
    private Etapa etapaInicial;

    @Enumerated(EnumType.STRING)
    private DiaSemana dia;

    @Column(nullable = false)
    private int ordem;

    @Version
    private long versao;

    // construtor exigido pelo JPA
    protected TarefaEntity() {
    }

    // id da tarefa (ex.: a1a)
    public String getId() { return id; }

    // etapa atual
    public Etapa getEtapa() { return etapa; }

    // muda a etapa atual
    public void setEtapa(Etapa etapa) { this.etapa = etapa; }

    // volta a tarefa para a etapa em que o quadro começou
    public void restaurar() { this.etapa = etapaInicial; }

    // converte para o tipo de domínio
    public Tarefa paraDominio() {
        return new Tarefa(id, titulo, descricao, projeto, etapa, dia);
    }
}
// fim de TarefaEntity.java
