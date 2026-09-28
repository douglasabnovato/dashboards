// Fórmulas do simulador de funil do Guia original: leads, matrículas, faturamento, lucro e ROAS
package com.learntech.dashboards.core.simulador;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SimuladorFunil {

    private static final BigDecimal DIAS = BigDecimal.valueOf(30);
    private static final BigDecimal CEM = BigDecimal.valueOf(100);
    private static final BigDecimal ORCAMENTO_BAIXO = BigDecimal.valueOf(20);
    private static final BigDecimal ROAS_TERMOMETRO_CHEIO = new BigDecimal("6.0");

    // classe utilitária, não deve ser instanciada
    private SimuladorFunil() {
    }

    // aplica as fórmulas: leads = (orçamento × 30) ÷ CPA; matrículas = leads × conversão; faturamento = matrículas × ticket
    public static ResultadoSimulacao simular(ParametrosSimulacao p) {
        BigDecimal investimento = p.orcamentoDiario().multiply(DIAS);
        BigDecimal conversas = investimento.divide(p.cpa(), 6, RoundingMode.HALF_UP);
        BigDecimal matriculas = conversas.multiply(p.conversaoPercentual()).divide(CEM, 6, RoundingMode.HALF_UP);
        BigDecimal faturamento = matriculas.multiply(p.ticketMedio());
        BigDecimal lucro = faturamento.subtract(investimento);
        BigDecimal roas = faturamento.divide(investimento, 2, RoundingMode.HALF_UP);
        FaixaRoas faixa = FaixaRoas.de(roas);
        return new ResultadoSimulacao(p,
                investimento.setScale(0, RoundingMode.HALF_UP),
                conversas.setScale(0, RoundingMode.HALF_UP),
                matriculas.setScale(1, RoundingMode.HALF_UP),
                faturamento.setScale(0, RoundingMode.HALF_UP),
                lucro.setScale(0, RoundingMode.HALF_UP),
                roas,
                percentualTermometro(roas),
                faixa,
                diagnostico(p, roas, faixa));
    }

    // largura da barra do termômetro: ROAS ÷ 6 em %, limitada entre 5 e 100 (como no original)
    static int percentualTermometro(BigDecimal roas) {
        int pct = roas.multiply(CEM).divide(ROAS_TERMOMETRO_CHEIO, 0, RoundingMode.HALF_UP).intValue();
        return Math.max(5, Math.min(100, pct));
    }

    // nota do especialista; usa as mesmas faixas do termômetro para não se contradizer
    static String diagnostico(ParametrosSimulacao p, BigDecimal roas, FaixaRoas faixa) {
        String roasTxt = roas.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
        if (p.orcamentoDiario().compareTo(ORCAMENTO_BAIXO) <= 0) {
            return "Alerta de Orçamento Reduzido (R$ " + p.orcamentoDiario().stripTrailingZeros().toPlainString()
                    + "/dia): Tal como observado na Fase 3 do histórico real, verbas na faixa dos R$ 20/dia encarecem a "
                    + "conversa no WhatsApp (+24%). Mantenha entre R$ 40 e R$ 60/dia.";
        }
        if (faixa == FaixaRoas.PREJUIZO || faixa == FaixaRoas.MARGEM_APERTADA) {
            return "Atenção à Margem (ROAS " + roasTxt + "x): O retorno está próximo do ponto de equilíbrio. Aumentar a "
                    + "taxa de conversão comercial no WhatsApp ou ajustar o Ticket Médio vai alavancar a rentabilidade "
                    + "sem gastar mais em anúncios.";
        }
        return "Projeção Favorável (ROAS " + roasTxt + "x): Para cada R$ 1,00 aplicado em Meta Ads, a ByteClass projeta "
                + "gerar R$ " + roasTxt + " em novas matrículas. Mantenha a produção de novas variações do vídeo campeão.";
    }
}
// fim de SimuladorFunil.java
