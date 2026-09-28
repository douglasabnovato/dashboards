// Tela do Kanban tático: filtros por BU e dia, colunas, movimentos com WIP=1 e restauração do quadro
package com.learntech.dashboards.web.view;

import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.FiltroTarefas;
import com.learntech.dashboards.core.kanban.Projeto;
import com.learntech.dashboards.core.kanban.Quadro;
import com.learntech.dashboards.core.kanban.RegrasKanban;
import com.learntech.dashboards.core.kanban.Tarefa;
import com.learntech.dashboards.core.kanban.WipExcedidoException;
import com.learntech.dashboards.web.api.ApiIndisponivelException;
import com.learntech.dashboards.web.api.DashboardsApiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component("kanban")
@Scope("view")
public class KanbanBean {

    private final DashboardsApiClient api;
    private String projeto = "";
    private String dia = FiltroTarefas.FiltroDia.TODOS.name();
    private Quadro quadro;
    private String erro;
    private String aviso;

    // recebe o cliente da API
    public KanbanBean(DashboardsApiClient api) {
        this.api = api;
    }

    // carrega o quadro sem filtros
    @PostConstruct
    public void iniciar() {
        carregar();
    }

    // busca o quadro com os filtros atuais
    public void carregar() {
        try {
            quadro = api.quadro(projeto == null || projeto.isBlank() ? null : Projeto.valueOf(projeto),
                    dia == null || dia.isBlank() ? FiltroTarefas.FiltroDia.TODOS : FiltroTarefas.FiltroDia.valueOf(dia));
            erro = null;
        } catch (ApiIndisponivelException e) {
            quadro = null;
            erro = "Não foi possível carregar o quadro agora. A API pode estar iniciando; tente de novo.";
        }
    }

    // move a tarefa para a etapa indicada; WIP excedido vira aviso, sem mover
    public void mover(String id, String etapa) {
        aviso = null;
        try {
            api.mover(id, Etapa.valueOf(etapa));
        } catch (WipExcedidoException e) {
            aviso = e.getMessage();
        } catch (ApiIndisponivelException e) {
            aviso = "O movimento não foi salvo: " + e.getMessage();
        }
        carregar();
    }

    // devolve todas as tarefas à etapa inicial
    public void restaurar() {
        aviso = null;
        try {
            api.restaurar();
            aviso = "Quadro restaurado para o estado inicial.";
        } catch (ApiIndisponivelException e) {
            aviso = "Não foi possível restaurar: " + e.getMessage();
        }
        carregar();
    }

    // colunas na ordem do fluxo
    public List<Etapa> getEtapas() { return Arrays.asList(Etapa.values()); }

    // cartões de uma coluna
    public List<Tarefa> tarefas(Etapa etapa) {
        return quadro == null ? List.of() : quadro.daEtapa(etapa);
    }

    // BUs para o filtro
    public List<Projeto> getProjetos() { return Arrays.asList(Projeto.values()); }

    // opções do filtro de dia
    public List<FiltroTarefas.FiltroDia> getDias() { return Arrays.asList(FiltroTarefas.FiltroDia.values()); }

    // rótulo amigável da opção de dia
    public String rotuloDia(FiltroTarefas.FiltroDia d) {
        return switch (d) {
            case TODOS -> "Todos";
            case SEM_DIA -> "Sem dia definido";
            default -> com.learntech.dashboards.core.kanban.DiaSemana.valueOf(d.name()).nome();
        };
    }

    // Doing já está no limite (para destacar o contador)
    public boolean isWipCheio() {
        return quadro != null && quadro.emAndamento() >= RegrasKanban.WIP_LIMIT;
    }

    // filtro de BU ("" = todas)
    public String getProjeto() { return projeto; }

    // altera o filtro de BU
    public void setProjeto(String projeto) { this.projeto = projeto; }

    // filtro de dia
    public String getDia() { return dia; }

    // altera o filtro de dia
    public void setDia(String dia) { this.dia = dia; }

    // quadro atual (nulo se a API falhou)
    public Quadro getQuadro() { return quadro; }

    // erro de carregamento
    public String getErro() { return erro; }

    // aviso do último movimento
    public String getAviso() { return aviso; }
}
// fim de KanbanBean.java
