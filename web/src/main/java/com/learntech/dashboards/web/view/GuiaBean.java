// Tela do Guia: números de cada fase calculados pela API (não digitados à mão) e matriz de ROAS
package com.learntech.dashboards.web.view;

import com.learntech.dashboards.core.metricas.Fase;
import com.learntech.dashboards.core.metricas.PainelMetricas;
import com.learntech.dashboards.core.simulador.FaixaRoas;
import com.learntech.dashboards.web.api.ApiIndisponivelException;
import com.learntech.dashboards.web.api.DashboardsApiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("guia")
@Scope("request")
public class GuiaBean {

    private final DashboardsApiClient api;
    private final List<PainelMetricas> fases = new ArrayList<>();
    private String erro;

    // recebe o cliente da API
    public GuiaBean(DashboardsApiClient api) {
        this.api = api;
    }

    // busca o resumo de cada fase
    @PostConstruct
    public void iniciar() {
        try {
            for (Fase f : Fase.values()) {
                fases.add(api.painel(f.numero()));
            }
        } catch (ApiIndisponivelException e) {
            fases.clear();
            erro = "Os números das fases não puderam ser carregados agora.";
        }
    }

    // painéis das três fases
    public List<PainelMetricas> getFases() { return fases; }

    // faixas de ROAS
    public List<FaixaRoas> getFaixas() { return Arrays.asList(FaixaRoas.values()); }

    // mensagem de erro, se houver
    public String getErro() { return erro; }
}
// fim de GuiaBean.java
