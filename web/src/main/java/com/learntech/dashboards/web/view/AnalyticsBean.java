// Tela de histórico (Analytics): filtro de fase, KPIs, gráficos, tabela e diagnóstico
package com.learntech.dashboards.web.view;

import com.learntech.dashboards.core.metricas.PainelMetricas;
import com.learntech.dashboards.web.api.ApiIndisponivelException;
import com.learntech.dashboards.web.api.DashboardsApiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("analytics")
@Scope("view")
public class AnalyticsBean {

    /** CTR informado no relatório original (a base não tem impressões para recalcular). */
    public static final String CTR_RELATORIO = "1,34%";

    private final DashboardsApiClient api;
    private String fase = "";
    private PainelMetricas painel;
    private String erro;

    // recebe o cliente da API
    public AnalyticsBean(DashboardsApiClient api) {
        this.api = api;
    }

    // carrega todas as fases ao abrir a tela
    @PostConstruct
    public void iniciar() {
        carregar();
    }

    // busca o painel na API para a fase selecionada ("" = todas)
    public void carregar() {
        try {
            painel = api.painel(faseNumerica());
            erro = null;
        } catch (ApiIndisponivelException e) {
            painel = null;
            erro = "Não foi possível carregar os dados agora. A API pode estar iniciando; tente de novo em alguns segundos.";
        }
    }

    // converte a opção da tela; valor vazio ou inválido = todas as fases
    private Integer faseNumerica() {
        try {
            return fase == null || fase.isBlank() ? null : Integer.valueOf(fase);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // configuração do gráfico de investimento x conversas
    public String getGraficoInvestimento() {
        return painel == null ? "{}" : GraficosJson.investimentoVsConversas(painel.meses());
    }

    // configuração do gráfico de eficiência
    public String getGraficoEficiencia() {
        return painel == null ? "{}" : GraficosJson.eficiencia(painel.meses());
    }

    // fase selecionada
    public String getFase() { return fase; }

    // altera a fase selecionada
    public void setFase(String fase) { this.fase = fase; }

    // painel carregado (nulo se a API falhou)
    public PainelMetricas getPainel() { return painel; }

    // mensagem de erro, se houver
    public String getErro() { return erro; }

    // CTR do relatório
    public String getCtr() { return CTR_RELATORIO; }
}
// fim de AnalyticsBean.java
