// Endpoints do histórico de tráfego pago: painel com filtro de fase e lista de fases
package com.learntech.dashboards.api.metricas;

import com.learntech.dashboards.core.metricas.Fase;
import com.learntech.dashboards.core.metricas.MetricaMensal;
import com.learntech.dashboards.core.metricas.PainelMetricas;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MetricasController {

    private final MetricaMensalRepository repositorio;

    // recebe o repositório por injeção
    public MetricasController(MetricaMensalRepository repositorio) {
        this.repositorio = repositorio;
    }

    // GET /api/metricas?fase=1..3 (sem fase = todas); fase inválida responde 400
    @GetMapping("/metricas")
    public PainelMetricas painel(@RequestParam(required = false) Integer fase) {
        List<MetricaMensal> meses = repositorio.findAll().stream().map(MetricaMensalEntity::paraDominio).toList();
        return PainelMetricas.de(meses, fase == null ? null : Fase.deNumero(fase));
    }

    // GET /api/fases: número, rótulo e período de cada fase
    @GetMapping("/fases")
    public List<Map<String, Object>> fases() {
        return Arrays.stream(Fase.values())
                .map(f -> Map.<String, Object>of("numero", f.numero(), "rotulo", f.rotulo(), "periodo", f.periodo()))
                .toList();
    }
}
// fim de MetricasController.java
