// Consolida as métricas mensais: totais, CPA e CPC ponderados e filtro por fase
package com.learntech.dashboards.core.metricas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class CalculadoraMetricas {

    // classe utilitária, não deve ser instanciada
    private CalculadoraMetricas() {
    }

    // devolve os meses da fase informada (ou todos, se vazio), em ordem cronológica
    public static List<MetricaMensal> filtrar(List<MetricaMensal> meses, Optional<Fase> fase) {
        return meses.stream()
                .filter(m -> fase.isEmpty() || m.fase() == fase.get())
                .sorted(Comparator.comparing(MetricaMensal::competencia))
                .toList();
    }

    // calcula os KPIs; CPA = gasto ÷ conversas e CPC = gasto ÷ cliques (ponderados, não média simples dos meses)
    public static ResumoMetricas resumir(List<MetricaMensal> meses) {
        BigDecimal gasto = meses.stream().map(MetricaMensal::gasto).reduce(BigDecimal.ZERO, BigDecimal::add);
        int cliques = meses.stream().mapToInt(MetricaMensal::cliques).sum();
        int conversas = meses.stream().mapToInt(MetricaMensal::conversas).sum();
        return new ResumoMetricas(
                meses.size(),
                gasto,
                dividir(gasto, meses.size()),
                cliques,
                conversas,
                dividir(gasto, conversas),
                dividir(gasto, cliques));
    }

    // divisão com duas casas; zero quando o divisor é zero (conjunto vazio)
    private static BigDecimal dividir(BigDecimal valor, int divisor) {
        if (divisor == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return valor.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
    }
}
// fim de CalculadoraMetricas.java
