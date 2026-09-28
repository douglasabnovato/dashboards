// Testes dos KPIs consolidados contra os números publicados no relatório original
package com.learntech.dashboards.core;

import com.learntech.dashboards.core.metricas.CalculadoraMetricas;
import com.learntech.dashboards.core.metricas.Fase;
import com.learntech.dashboards.core.metricas.MetricaMensal;
import com.learntech.dashboards.core.metricas.PainelMetricas;
import com.learntech.dashboards.core.metricas.ResumoMetricas;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraMetricasTest {

    // todas as fases: 1.007 conversas, CPA R$ 9,27 e CPC ponderado R$ 0,94 (9.936 cliques), como no relatório
    @Test
    void resumoGeralBateComRelatorio() {
        ResumoMetricas r = CalculadoraMetricas.resumir(HistoricoFixture.meses());
        assertEquals(10, r.meses());
        assertEquals(1007, r.conversasTotais());
        assertEquals(9936, r.cliquesTotais());
        assertEquals(new BigDecimal("9.27"), r.cpaMedio());
        assertEquals(new BigDecimal("0.94"), r.cpcMedio());
        assertEquals(new BigDecimal("9336"), r.investimentoTotal());
    }

    // fase 1: R$ 3.413, 307 conversas (Guia original)
    @Test
    void resumoDaFaseAprendizado() {
        List<MetricaMensal> f1 = CalculadoraMetricas.filtrar(HistoricoFixture.meses(), Optional.of(Fase.APRENDIZADO));
        ResumoMetricas r = CalculadoraMetricas.resumir(f1);
        assertEquals(3, r.meses());
        assertEquals(new BigDecimal("3413"), r.investimentoTotal());
        assertEquals(307, r.conversasTotais());
        assertEquals(new BigDecimal("11.12"), r.cpaMedio());
    }

    // fase 3: R$ 1.756, 182 conversas, CPA R$ 9,65 (Guia original)
    @Test
    void resumoDaFaseManutencao() {
        ResumoMetricas r = CalculadoraMetricas.resumir(
                CalculadoraMetricas.filtrar(HistoricoFixture.meses(), Optional.of(Fase.MANUTENCAO)));
        assertEquals(new BigDecimal("1756"), r.investimentoTotal());
        assertEquals(182, r.conversasTotais());
        assertEquals(new BigDecimal("9.65"), r.cpaMedio());
    }

    // conjunto vazio não divide por zero
    @Test
    void resumoVazioRetornaZeros() {
        ResumoMetricas r = CalculadoraMetricas.resumir(List.of());
        assertEquals(0, r.conversasTotais());
        assertEquals(new BigDecimal("0.00"), r.cpaMedio());
    }

    // painel sem filtro usa a análise geral; com filtro usa a da fase e só os meses dela
    @Test
    void painelPorFase() {
        PainelMetricas geral = PainelMetricas.de(HistoricoFixture.meses(), null);
        assertEquals("Todas as Fases (10 Meses)", geral.rotuloFase());
        assertEquals(Fase.ANALISE_GERAL, geral.analise());
        PainelMetricas f2 = PainelMetricas.de(HistoricoFixture.meses(), Fase.VIRADA_DE_CHAVE);
        assertEquals(4, f2.meses().size());
        assertEquals("Jan/26", f2.meses().get(0).rotulo());
        assertEquals(518, f2.resumo().conversasTotais());
        assertEquals(Integer.valueOf(2), f2.fase());
    }

    // fase inexistente é rejeitada
    @Test
    void faseInexistente() {
        assertThrows(IllegalArgumentException.class, () -> Fase.deNumero(9));
    }
}
// fim de CalculadoraMetricasTest.java
