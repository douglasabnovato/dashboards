// Cenários prontos do simulador (três do dashboard v2 e o padrão real do dashboard v1)
package com.learntech.dashboards.core.simulador;

import java.math.BigDecimal;

public enum Preset {
    RETOMADA_INTELIGENTE("Retomada Inteligente", "50", "8.0", "5.0", "800"),
    HIGH_TICKET_BOOTCAMP("High Ticket Bootcamp", "60", "12.0", "4.0", "1800"),
    ALERTA_BAIXA_CONVERSAO("Alerta Baixa Conversão", "30", "10.0", "1.5", "500"),
    BASELINE_V1("Baseline v1 (histórico real)", "30", "9.27", "5.0", "800");

    private final String rotulo;
    private final ParametrosSimulacao parametros;

    // monta os parâmetros do cenário a partir dos valores originais
    Preset(String rotulo, String orcamento, String cpa, String conversao, String ticket) {
        this.rotulo = rotulo;
        this.parametros = new ParametrosSimulacao(new BigDecimal(orcamento), new BigDecimal(cpa),
                new BigDecimal(conversao), new BigDecimal(ticket));
    }

    // nome exibido no botão
    public String rotulo() { return rotulo; }

    // valores das alavancas do cenário
    public ParametrosSimulacao parametros() { return parametros; }

    // cenário carregado ao abrir o simulador ou restaurar o padrão (igual ao v2)
    public static Preset padrao() { return RETOMADA_INTELIGENTE; }
}
// fim de Preset.java
