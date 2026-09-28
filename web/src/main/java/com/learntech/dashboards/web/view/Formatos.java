// Formatação pt-BR usada nas páginas: moeda, decimal e inteiro (bean "fmt" no EL)
package com.learntech.dashboards.web.view;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component("fmt")
public class Formatos {

    private static final Locale BR = Locale.forLanguageTag("pt-BR");

    // R$ com duas casas quando houver centavos (R$ 9,27) e sem casas quando inteiro (R$ 9.336)
    public String moeda(BigDecimal valor) {
        if (valor == null) {
            return "—";
        }
        NumberFormat f = NumberFormat.getNumberInstance(BR);
        boolean inteiro = valor.stripTrailingZeros().scale() <= 0;
        f.setMinimumFractionDigits(inteiro ? 0 : 2);
        f.setMaximumFractionDigits(inteiro ? 0 : 2);
        return "R$ " + f.format(valor);
    }

    // número com casas fixas (ex.: 9,4 ou 5,00)
    public String decimal(BigDecimal valor, int casas) {
        if (valor == null) {
            return "—";
        }
        NumberFormat f = NumberFormat.getNumberInstance(BR);
        f.setMinimumFractionDigits(casas);
        f.setMaximumFractionDigits(casas);
        return f.format(valor);
    }

    // inteiro com separador de milhar (1.007)
    public String inteiro(long valor) {
        return NumberFormat.getIntegerInstance(BR).format(valor);
    }
}
// fim de Formatos.java
