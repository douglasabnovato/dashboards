// Linha da tabela metrica_mensal (histórico Meta Ads), convertida para o record do core
package com.learntech.dashboards.api.metricas;

import com.learntech.dashboards.core.metricas.Fase;
import com.learntech.dashboards.core.metricas.MetricaMensal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "metrica_mensal")
public class MetricaMensalEntity {

    @Id
    private Integer id;

    @Column(name = "ano_mes", nullable = false)
    private String anoMes;

    @Column(nullable = false)
    private String rotulo;

    @Column(nullable = false)
    private BigDecimal gasto;

    @Column(nullable = false)
    private int cliques;

    @Column(nullable = false)
    private int conversas;

    @Column(nullable = false)
    private BigDecimal cpc;

    @Column(nullable = false)
    private BigDecimal cpa;

    @Column(nullable = false)
    private short fase;

    // construtor exigido pelo JPA
    protected MetricaMensalEntity() {
    }

    // converte para o tipo de domínio usado nos cálculos e na resposta
    public MetricaMensal paraDominio() {
        return new MetricaMensal(YearMonth.parse(anoMes), rotulo, gasto.stripTrailingZeros().scale() <= 0
                ? gasto.setScale(0) : gasto, cliques, conversas, cpc, cpa, Fase.deNumero(fase));
    }
}
// fim de MetricaMensalEntity.java
