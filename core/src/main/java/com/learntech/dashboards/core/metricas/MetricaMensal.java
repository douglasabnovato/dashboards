// Linha mensal do relatório de tráfego pago (valores como vieram do relatório auditado)
package com.learntech.dashboards.core.metricas;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

public record MetricaMensal(YearMonth competencia, String rotulo, BigDecimal gasto, int cliques, int conversas,
                            BigDecimal cpc, BigDecimal cpa, Fase fase) {

    // valida os campos obrigatórios e impede números negativos
    public MetricaMensal {
        Objects.requireNonNull(competencia, "competencia");
        Objects.requireNonNull(rotulo, "rotulo");
        Objects.requireNonNull(gasto, "gasto");
        Objects.requireNonNull(cpc, "cpc");
        Objects.requireNonNull(cpa, "cpa");
        Objects.requireNonNull(fase, "fase");
        if (gasto.signum() < 0 || cliques < 0 || conversas < 0 || cpc.signum() < 0 || cpa.signum() < 0) {
            throw new IllegalArgumentException("Métrica com valor negativo em " + rotulo);
        }
    }
}
// fim de MetricaMensal.java
