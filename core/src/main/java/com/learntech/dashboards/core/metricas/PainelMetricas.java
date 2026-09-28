// Resposta do painel de histórico: fase filtrada (ou nula), análise, KPIs e meses
package com.learntech.dashboards.core.metricas;

import java.util.List;

public record PainelMetricas(Integer fase, String rotuloFase, String analise, ResumoMetricas resumo,
                             List<MetricaMensal> meses) {

    // monta o painel a partir dos meses já carregados; fase nula significa todas as fases
    public static PainelMetricas de(List<MetricaMensal> todos, Fase fase) {
        List<MetricaMensal> meses = CalculadoraMetricas.filtrar(todos, java.util.Optional.ofNullable(fase));
        return new PainelMetricas(
                fase == null ? null : fase.numero(),
                fase == null ? "Todas as Fases (" + meses.size() + " Meses)" : fase.rotulo() + " (" + fase.periodo() + ")",
                fase == null ? Fase.ANALISE_GERAL : fase.analise(),
                CalculadoraMetricas.resumir(meses),
                meses);
    }
}
// fim de PainelMetricas.java
