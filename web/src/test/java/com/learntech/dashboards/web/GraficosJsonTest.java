// Testes do JSON Chart.js gerado para o p:chart
package com.learntech.dashboards.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learntech.dashboards.core.metricas.Fase;
import com.learntech.dashboards.core.metricas.MetricaMensal;
import com.learntech.dashboards.web.view.GraficosJson;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraficosJsonTest {

    private final List<MetricaMensal> meses = List.of(
            new MetricaMensal(YearMonth.of(2025, 10), "Out/25", new BigDecimal("985"), 619, 87,
                    new BigDecimal("1.59"), new BigDecimal("11.32"), Fase.APRENDIZADO),
            new MetricaMensal(YearMonth.of(2026, 1), "Jan/26", new BigDecimal("1411"), 2203, 193,
                    new BigDecimal("0.64"), new BigDecimal("7.31"), Fase.VIRADA_DE_CHAVE));

    // gráfico combinado: barras de gasto e linha de conversas no eixo y1
    @Test
    void investimentoVsConversas() throws Exception {
        JsonNode cfg = new ObjectMapper().readTree(GraficosJson.investimentoVsConversas(meses));
        assertEquals("bar", cfg.get("type").asText());
        assertEquals("Out/25", cfg.at("/data/labels/0").asText());
        assertEquals(985, cfg.at("/data/datasets/0/data/0").asInt());
        assertEquals("line", cfg.at("/data/datasets/1/type").asText());
        assertEquals("y1", cfg.at("/data/datasets/1/yAxisID").asText());
        assertEquals(193, cfg.at("/data/datasets/1/data/1").asInt());
    }

    // gráfico de eficiência com CPC e CPA
    @Test
    void eficiencia() throws Exception {
        JsonNode cfg = new ObjectMapper().readTree(GraficosJson.eficiencia(meses));
        assertEquals("line", cfg.get("type").asText());
        assertEquals(7.31, cfg.at("/data/datasets/1/data/1").asDouble());
    }
}
// fim de GraficosJsonTest.java
