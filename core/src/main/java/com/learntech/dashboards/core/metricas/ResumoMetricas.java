// Indicadores consolidados de um conjunto de meses (KPIs do topo do dashboard)
package com.learntech.dashboards.core.metricas;

import java.math.BigDecimal;

public record ResumoMetricas(int meses, BigDecimal investimentoTotal, BigDecimal mediaMensal, int cliquesTotais,
                             int conversasTotais, BigDecimal cpaMedio, BigDecimal cpcMedio) {
}
// fim de ResumoMetricas.java
