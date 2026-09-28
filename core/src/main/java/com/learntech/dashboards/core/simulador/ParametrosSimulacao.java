// Alavancas do simulador de funil, com os mesmos limites dos controles do dashboard original
package com.learntech.dashboards.core.simulador;

import java.math.BigDecimal;
import java.util.Objects;

public record ParametrosSimulacao(BigDecimal orcamentoDiario, BigDecimal cpa, BigDecimal conversaoPercentual,
                                  BigDecimal ticketMedio) {

    public static final BigDecimal ORCAMENTO_MIN = new BigDecimal("10");
    public static final BigDecimal ORCAMENTO_MAX = new BigDecimal("200");
    public static final BigDecimal CPA_MIN = new BigDecimal("5");
    public static final BigDecimal CPA_MAX = new BigDecimal("25");
    public static final BigDecimal CONVERSAO_MIN = new BigDecimal("1");
    public static final BigDecimal CONVERSAO_MAX = new BigDecimal("15");
    public static final BigDecimal TICKET_MIN = new BigDecimal("200");
    public static final BigDecimal TICKET_MAX = new BigDecimal("3000");

    // rejeita valores fora das faixas do simulador (R$ 10-200/dia, CPA R$ 5-25, conversão 1-15%, ticket R$ 200-3.000)
    public ParametrosSimulacao {
        faixa("orcamentoDiario", orcamentoDiario, ORCAMENTO_MIN, ORCAMENTO_MAX);
        faixa("cpa", cpa, CPA_MIN, CPA_MAX);
        faixa("conversaoPercentual", conversaoPercentual, CONVERSAO_MIN, CONVERSAO_MAX);
        faixa("ticketMedio", ticketMedio, TICKET_MIN, TICKET_MAX);
    }

    // confere se o valor existe e está dentro da faixa fechada [min, max]
    private static void faixa(String campo, BigDecimal valor, BigDecimal min, BigDecimal max) {
        Objects.requireNonNull(valor, campo);
        if (valor.compareTo(min) < 0 || valor.compareTo(max) > 0) {
            throw new IllegalArgumentException(campo + " deve estar entre " + min.toPlainString() + " e "
                    + max.toPlainString());
        }
    }
}
// fim de ParametrosSimulacao.java
