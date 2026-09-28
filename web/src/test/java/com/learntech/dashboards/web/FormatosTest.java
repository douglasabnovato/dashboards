// Testes da formatação pt-BR usada nas páginas
package com.learntech.dashboards.web;

import com.learntech.dashboards.web.view.Formatos;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatosTest {

    private final Formatos fmt = new Formatos();

    // moeda inteira sem centavos e com milhar; moeda com centavos com duas casas
    @Test
    void moeda() {
        assertEquals("R$ 9.336", fmt.moeda(new BigDecimal("9336.00")));
        assertEquals("R$ 9,27", fmt.moeda(new BigDecimal("9.27")));
        assertEquals("R$ 0,70", fmt.moeda(new BigDecimal("0.70")));
        assertEquals("—", fmt.moeda(null));
    }

    // decimal com casas fixas e inteiro com milhar
    @Test
    void decimalEInteiro() {
        assertEquals("9,4", fmt.decimal(new BigDecimal("9.4"), 1));
        assertEquals("5,00", fmt.decimal(new BigDecimal("5"), 2));
        assertEquals("1.007", fmt.inteiro(1007));
    }
}
// fim de FormatosTest.java
