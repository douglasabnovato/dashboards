// Testes das fórmulas do simulador, presets e termômetro de ROAS
package com.learntech.dashboards.core;

import com.learntech.dashboards.core.simulador.FaixaRoas;
import com.learntech.dashboards.core.simulador.ParametrosSimulacao;
import com.learntech.dashboards.core.simulador.Preset;
import com.learntech.dashboards.core.simulador.ResultadoSimulacao;
import com.learntech.dashboards.core.simulador.SimuladorFunil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimuladorFunilTest {

    // Retomada Inteligente: R$ 1.500/mês, 188 conversas, 9,4 alunos, R$ 7.500, lucro R$ 6.000, ROAS 5,00x
    @Test
    void presetRetomada() {
        ResultadoSimulacao r = SimuladorFunil.simular(Preset.RETOMADA_INTELIGENTE.parametros());
        assertEquals(new BigDecimal("1500"), r.investimentoMensal());
        assertEquals(new BigDecimal("188"), r.conversasMes());
        assertEquals(new BigDecimal("9.4"), r.matriculas());
        assertEquals(new BigDecimal("7500"), r.faturamento());
        assertEquals(new BigDecimal("6000"), r.lucroBruto());
        assertEquals(new BigDecimal("5.00"), r.roas());
        assertEquals(FaixaRoas.ESCALA_DE_OURO, r.faixa());
        assertEquals(83, r.percentualTermometro());
    }

    // Alerta Baixa Conversão: ROAS 0,75x cai em prejuízo
    @Test
    void presetAlertaEhPrejuizo() {
        ResultadoSimulacao r = SimuladorFunil.simular(Preset.ALERTA_BAIXA_CONVERSAO.parametros());
        assertEquals(new BigDecimal("0.75"), r.roas());
        assertEquals(FaixaRoas.PREJUIZO, r.faixa());
        assertTrue(r.diagnostico().startsWith("Atenção à Margem"));
    }

    // High Ticket: ROAS 6,00x e barra cheia
    @Test
    void presetHighTicket() {
        ResultadoSimulacao r = SimuladorFunil.simular(Preset.HIGH_TICKET_BOOTCAMP.parametros());
        assertEquals(new BigDecimal("6.00"), r.roas());
        assertEquals(100, r.percentualTermometro());
    }

    // limites das faixas: 1,0 é margem; 2,5 é saudável; 4,5 é escala
    @Test
    void limitesDasFaixas() {
        assertEquals(FaixaRoas.PREJUIZO, FaixaRoas.de(new BigDecimal("0.99")));
        assertEquals(FaixaRoas.MARGEM_APERTADA, FaixaRoas.de(new BigDecimal("1.00")));
        assertEquals(FaixaRoas.SAUDAVEL, FaixaRoas.de(new BigDecimal("2.50")));
        assertEquals(FaixaRoas.ESCALA_DE_OURO, FaixaRoas.de(new BigDecimal("4.50")));
    }

    // orçamento de até R$ 20/dia gera o alerta da Fase 3
    @Test
    void alertaDeOrcamentoBaixo() {
        ParametrosSimulacao p = new ParametrosSimulacao(new BigDecimal("20"), new BigDecimal("8"),
                new BigDecimal("5"), new BigDecimal("800"));
        assertTrue(SimuladorFunil.simular(p).diagnostico().startsWith("Alerta de Orçamento Reduzido (R$ 20/dia)"));
    }

    // valores fora das faixas dos controles são rejeitados
    @Test
    void rejeitaForaDaFaixa() {
        assertThrows(IllegalArgumentException.class, () -> new ParametrosSimulacao(new BigDecimal("5"),
                new BigDecimal("8"), new BigDecimal("5"), new BigDecimal("800")));
        assertThrows(IllegalArgumentException.class, () -> new ParametrosSimulacao(new BigDecimal("50"),
                new BigDecimal("0"), new BigDecimal("5"), new BigDecimal("800")));
        assertThrows(NullPointerException.class, () -> new ParametrosSimulacao(null,
                new BigDecimal("8"), new BigDecimal("5"), new BigDecimal("800")));
    }
}
// fim de SimuladorFunilTest.java
