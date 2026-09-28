// Endpoints do simulador de funil: presets, matriz de ROAS e cálculo da projeção
package com.learntech.dashboards.api.simulador;

import com.learntech.dashboards.core.simulador.FaixaRoas;
import com.learntech.dashboards.core.simulador.ParametrosSimulacao;
import com.learntech.dashboards.core.simulador.Preset;
import com.learntech.dashboards.core.simulador.ResultadoSimulacao;
import com.learntech.dashboards.core.simulador.SimuladorFunil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/simulador")
public class SimuladorController {

    /** Preset exposto na API. */
    public record PresetDto(String codigo, String rotulo, boolean padrao, ParametrosSimulacao parametros) {
    }

    /** Faixa da matriz de decisão por ROAS. */
    public record FaixaDto(String codigo, String status, String minimo, String limite, String recomendacao) {
    }

    // GET /api/simulador/presets: cenários prontos, com o padrão marcado
    @GetMapping("/presets")
    public List<PresetDto> presets() {
        return Arrays.stream(Preset.values())
                .map(p -> new PresetDto(p.name(), p.rotulo(), p == Preset.padrao(), p.parametros()))
                .toList();
    }

    // GET /api/simulador/faixas: matriz de decisão estratégica
    @GetMapping("/faixas")
    public List<FaixaDto> faixas() {
        return Arrays.stream(FaixaRoas.values())
                .map(f -> new FaixaDto(f.name(), f.status(), f.minimo().toPlainString(),
                        f.limite() == null ? null : f.limite().toPlainString(), f.recomendacao()))
                .toList();
    }

    // POST /api/simulador: calcula a projeção; parâmetros fora das faixas respondem 400
    @PostMapping
    public ResultadoSimulacao simular(@RequestBody ParametrosSimulacao parametros) {
        return SimuladorFunil.simular(parametros);
    }
}
// fim de SimuladorController.java
