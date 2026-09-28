// Monta a configuração Chart.js (JSON) dos gráficos do histórico para o componente p:chart
package com.learntech.dashboards.web.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learntech.dashboards.core.metricas.MetricaMensal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GraficosJson {

    private static final ObjectMapper JSON = new ObjectMapper();

    // classe utilitária, não deve ser instanciada
    private GraficosJson() {
    }

    // barras de gasto (eixo esquerdo) + linha de conversas no WhatsApp (eixo direito), como no original
    public static String investimentoVsConversas(List<MetricaMensal> meses) {
        Map<String, Object> gasto = dataset("Gasto (R$)", meses.stream().map(MetricaMensal::gasto).toList(), "#4f46e5");
        gasto.put("type", "bar");
        gasto.put("order", 2);
        Map<String, Object> conversas = dataset("Conversas Whats",
                meses.stream().map(MetricaMensal::conversas).toList(), "#059669");
        conversas.put("type", "line");
        conversas.put("yAxisID", "y1");
        conversas.put("order", 1);
        Map<String, Object> escalas = Map.of(
                "y", Map.of("beginAtZero", true, "title", Map.of("display", true, "text", "R$")),
                "y1", Map.of("beginAtZero", true, "position", "right", "grid", Map.of("drawOnChartArea", false),
                        "title", Map.of("display", true, "text", "Conversas")));
        return config("bar", meses, List.of(gasto, conversas), escalas);
    }

    // linhas de CPC e custo por conversa
    public static String eficiencia(List<MetricaMensal> meses) {
        return config("line", meses, List.of(
                dataset("CPC (R$)", meses.stream().map(MetricaMensal::cpc).toList(), "#0284c7"),
                dataset("Custo/Conversa (R$)", meses.stream().map(MetricaMensal::cpa).toList(), "#6366f1")),
                Map.of("y", Map.of("beginAtZero", true)));
    }

    // um conjunto de dados com cor de borda e preenchimento
    private static Map<String, Object> dataset(String rotulo, List<?> dados, String cor) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("label", rotulo);
        d.put("data", dados);
        d.put("borderColor", cor);
        d.put("backgroundColor", cor);
        d.put("borderWidth", 2);
        d.put("tension", 0.3);
        return d;
    }

    // configuração completa serializada; rótulos do eixo X são os meses (Out/25...)
    private static String config(String tipo, List<MetricaMensal> meses, List<Map<String, Object>> datasets,
                                 Map<String, Object> escalas) {
        Map<String, Object> cfg = new LinkedHashMap<>();
        cfg.put("type", tipo);
        cfg.put("data", Map.of("labels", meses.stream().map(MetricaMensal::rotulo).toList(), "datasets", datasets));
        cfg.put("options", Map.of("responsive", true, "maintainAspectRatio", false, "scales", escalas,
                "plugins", Map.of("legend", Map.of("position", "bottom"))));
        try {
            return JSON.writeValueAsString(cfg);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Falha ao gerar JSON do gráfico", e);
        }
    }
}
// fim de GraficosJson.java
