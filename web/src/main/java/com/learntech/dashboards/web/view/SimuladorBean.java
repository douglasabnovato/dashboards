// Tela do simulador de funil: presets, alavancas, projeção, termômetro de ROAS e diagnóstico
package com.learntech.dashboards.web.view;

import com.learntech.dashboards.core.simulador.FaixaRoas;
import com.learntech.dashboards.core.simulador.ParametrosSimulacao;
import com.learntech.dashboards.core.simulador.Preset;
import com.learntech.dashboards.core.simulador.ResultadoSimulacao;
import com.learntech.dashboards.web.api.ApiIndisponivelException;
import com.learntech.dashboards.web.api.DashboardsApiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component("simulador")
@Scope("view")
public class SimuladorBean {

    private final DashboardsApiClient api;
    private BigDecimal orcamentoDiario;
    private BigDecimal cpa;
    private BigDecimal conversao;
    private BigDecimal ticket;
    private ResultadoSimulacao resultado;
    private String erro;

    // recebe o cliente da API
    public SimuladorBean(DashboardsApiClient api) {
        this.api = api;
    }

    // abre no preset padrão (Retomada Inteligente), como o original
    @PostConstruct
    public void iniciar() {
        aplicar(Preset.padrao().name());
    }

    // carrega os valores de um preset e recalcula
    public void aplicar(String codigo) {
        ParametrosSimulacao p = Preset.valueOf(codigo).parametros();
        orcamentoDiario = p.orcamentoDiario();
        cpa = p.cpa();
        conversao = p.conversaoPercentual();
        ticket = p.ticketMedio();
        simular();
    }

    // envia as alavancas para a API; valores fora da faixa ou API fora do ar viram mensagem na tela
    public void simular() {
        try {
            resultado = api.simular(new ParametrosSimulacao(orcamentoDiario, cpa, conversao, ticket));
            erro = null;
        } catch (IllegalArgumentException | NullPointerException e) {
            erro = "Confira os valores: " + e.getMessage();
        } catch (ApiIndisponivelException e) {
            resultado = null;
            erro = "Não foi possível calcular agora. A API pode estar iniciando; tente de novo em alguns segundos.";
        }
    }

    // presets na ordem de exibição
    public List<Preset> getPresets() {
        return Arrays.asList(Preset.values());
    }

    // matriz de decisão por ROAS
    public List<FaixaRoas> getFaixas() {
        return Arrays.asList(FaixaRoas.values());
    }

    // classe CSS da faixa atual (cor do termômetro)
    public String getClasseFaixa() {
        return resultado == null ? "" : "faixa-" + resultado.faixa().name().toLowerCase();
    }

    // orçamento diário (R$)
    public BigDecimal getOrcamentoDiario() { return orcamentoDiario; }

    // altera o orçamento diário
    public void setOrcamentoDiario(BigDecimal v) { this.orcamentoDiario = v; }

    // CPA esperado (R$)
    public BigDecimal getCpa() { return cpa; }

    // altera o CPA esperado
    public void setCpa(BigDecimal v) { this.cpa = v; }

    // conversão comercial (%)
    public BigDecimal getConversao() { return conversao; }

    // altera a conversão comercial
    public void setConversao(BigDecimal v) { this.conversao = v; }

    // ticket médio (R$)
    public BigDecimal getTicket() { return ticket; }

    // altera o ticket médio
    public void setTicket(BigDecimal v) { this.ticket = v; }

    // última projeção calculada
    public ResultadoSimulacao getResultado() { return resultado; }

    // mensagem de erro, se houver
    public String getErro() { return erro; }
}
// fim de SimuladorBean.java
