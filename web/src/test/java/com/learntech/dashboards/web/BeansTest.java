// Testes das telas (beans) com a API simulada: erro de rede, WIP excedido e presets
package com.learntech.dashboards.web;

import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.Quadro;
import com.learntech.dashboards.core.kanban.WipExcedidoException;
import com.learntech.dashboards.core.simulador.Preset;
import com.learntech.dashboards.core.simulador.SimuladorFunil;
import com.learntech.dashboards.web.api.ApiIndisponivelException;
import com.learntech.dashboards.web.api.DashboardsApiClient;
import com.learntech.dashboards.web.view.AnalyticsBean;
import com.learntech.dashboards.web.view.KanbanBean;
import com.learntech.dashboards.web.view.SimuladorBean;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BeansTest {

    // API fora do ar: a tela mostra mensagem em vez de erro 500
    @Test
    void analyticsComApiForaDoAr() {
        DashboardsApiClient api = mock(DashboardsApiClient.class);
        when(api.painel(isNull())).thenThrow(new ApiIndisponivelException("x", null));
        AnalyticsBean bean = new AnalyticsBean(api);
        bean.iniciar();
        assertNull(bean.getPainel());
        assertNotNull(bean.getErro());
        assertEquals("{}", bean.getGraficoInvestimento());
    }

    // simulador abre no preset Retomada e troca de preset
    @Test
    void simuladorAplicaPresets() {
        DashboardsApiClient api = mock(DashboardsApiClient.class);
        when(api.simular(any())).thenAnswer(inv -> SimuladorFunil.simular(inv.getArgument(0)));
        SimuladorBean bean = new SimuladorBean(api);
        bean.iniciar();
        assertEquals(new BigDecimal("50"), bean.getOrcamentoDiario());
        assertEquals(new BigDecimal("5.00"), bean.getResultado().roas());
        bean.aplicar(Preset.ALERTA_BAIXA_CONVERSAO.name());
        assertEquals("faixa-prejuizo", bean.getClasseFaixa());
    }

    // valor fora da faixa digitado vira mensagem, sem chamar a API
    @Test
    void simuladorValidaEntrada() {
        DashboardsApiClient api = mock(DashboardsApiClient.class);
        when(api.simular(any())).thenAnswer(inv -> SimuladorFunil.simular(inv.getArgument(0)));
        SimuladorBean bean = new SimuladorBean(api);
        bean.iniciar();
        bean.setOrcamentoDiario(new BigDecimal("999"));
        bean.simular();
        assertTrue(bean.getErro().startsWith("Confira os valores"));
    }

    // WIP excedido vira aviso na tela e o quadro é recarregado
    @Test
    void kanbanAvisaWip() {
        DashboardsApiClient api = mock(DashboardsApiClient.class);
        when(api.quadro(any(), any())).thenReturn(new Quadro("27-07-2026 a 31-07-2026", 1, 1, List.of()));
        when(api.mover(eq("a1a"), eq(Etapa.DOING))).thenThrow(new WipExcedidoException("b3a"));
        KanbanBean bean = new KanbanBean(api);
        bean.iniciar();
        bean.mover("a1a", "DOING");
        assertTrue(bean.getAviso().contains("b3a"));
        assertTrue(bean.isWipCheio());
    }
}
// fim de BeansTest.java
