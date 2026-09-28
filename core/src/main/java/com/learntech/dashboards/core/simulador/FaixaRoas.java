// Termômetro de ROAS: faixas, status e ação recomendada (limiares do termômetro do dashboard v2)
package com.learntech.dashboards.core.simulador;

import java.math.BigDecimal;

public enum FaixaRoas {
    PREJUIZO("PREJUÍZO EM ANÚNCIOS", "0", "1.0",
            "A receita não cobre os custos de Meta Ads. Pausar campanhas ou reestruturar o atendimento no WhatsApp imediatamente!"),
    MARGEM_APERTADA("MARGEM APERTADA", "1.0", "2.5",
            "Operação no ponto de equilíbrio. Focar em aumentar a conversão comercial ou subir o ticket dos cursos."),
    SAUDAVEL("CENÁRIO SAUDÁVEL", "2.5", "4.5",
            "Excelente rentabilidade operacional. Manter o orçamento e testar novos vídeos antes do desgaste dos criativos."),
    ESCALA_DE_OURO("ESCALA DE OURO", "4.5", null,
            "Retorno altíssimo! Recomenda-se aumentar a verba diária imediatamente para capturar mais mercado.");

    private final String status;
    private final BigDecimal minimo;
    private final BigDecimal limite;
    private final String recomendacao;

    // guarda a faixa [minimo, limite) e os textos
    FaixaRoas(String status, String minimo, String limite, String recomendacao) {
        this.status = status;
        this.minimo = new BigDecimal(minimo);
        this.limite = limite == null ? null : new BigDecimal(limite);
        this.recomendacao = recomendacao;
    }

    // texto do selo de status
    public String status() { return status; }

    // início da faixa (inclusivo)
    public BigDecimal minimo() { return minimo; }

    // fim da faixa (exclusivo); nulo na última faixa
    public BigDecimal limite() { return limite; }

    // ação recomendada para a faixa
    public String recomendacao() { return recomendacao; }

    // classifica um ROAS na faixa correspondente
    public static FaixaRoas de(BigDecimal roas) {
        for (FaixaRoas f : values()) {
            if (f.limite == null || roas.compareTo(f.limite) < 0) {
                return f;
            }
        }
        return ESCALA_DE_OURO;
    }
}
// fim de FaixaRoas.java
