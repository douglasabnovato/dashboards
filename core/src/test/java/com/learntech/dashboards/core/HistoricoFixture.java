// Dados mensais do relatório original (Out/25 a Jul/26), usados como fixture dos testes
package com.learntech.dashboards.core;

import com.learntech.dashboards.core.metricas.Fase;
import com.learntech.dashboards.core.metricas.MetricaMensal;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public final class HistoricoFixture {

    // classe utilitária, não deve ser instanciada
    private HistoricoFixture() {
    }

    // devolve os 10 meses exatamente como no dashboard original
    public static List<MetricaMensal> meses() {
        return List.of(
                m(2025, 10, "Out/25", 985, 619, 87, "1.59", "11.32", 1),
                m(2025, 11, "Nov/25", 1328, 934, 106, "1.42", "12.53", 1),
                m(2025, 12, "Dez/25", 1100, 836, 114, "1.32", "9.65", 1),
                m(2026, 1, "Jan/26", 1411, 2203, 193, "0.64", "7.31", 2),
                m(2026, 2, "Fev/26", 1141, 1629, 139, "0.70", "8.21", 2),
                m(2026, 3, "Mar/26", 948, 1208, 113, "0.78", "8.38", 2),
                m(2026, 4, "Abr/26", 667, 1036, 73, "0.64", "9.13", 2),
                m(2026, 5, "Mai/26", 610, 521, 66, "1.17", "9.24", 3),
                m(2026, 6, "Jun/26", 612, 466, 64, "1.31", "9.57", 3),
                m(2026, 7, "Jul/26", 534, 484, 52, "1.10", "10.27", 3));
    }

    // monta uma linha mensal
    private static MetricaMensal m(int ano, int mes, String rotulo, int gasto, int cliques, int conversas,
                                   String cpc, String cpa, int fase) {
        return new MetricaMensal(YearMonth.of(ano, mes), rotulo, BigDecimal.valueOf(gasto), cliques, conversas,
                new BigDecimal(cpc), new BigDecimal(cpa), Fase.deNumero(fase));
    }
}
// fim de HistoricoFixture.java
