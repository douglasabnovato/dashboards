// Projeção mensal (30 dias) calculada pelo simulador
package com.learntech.dashboards.core.simulador;

import java.math.BigDecimal;

public record ResultadoSimulacao(ParametrosSimulacao parametros, BigDecimal investimentoMensal,
                                 BigDecimal conversasMes, BigDecimal matriculas, BigDecimal faturamento,
                                 BigDecimal lucroBruto, BigDecimal roas, int percentualTermometro,
                                 FaixaRoas faixa, String diagnostico) {
}
// fim de ResultadoSimulacao.java
