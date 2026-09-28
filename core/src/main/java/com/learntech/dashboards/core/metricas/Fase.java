// Fases históricas da conta Meta Ads da ByteClass, com rótulo, período e análise do relatório original
package com.learntech.dashboards.core.metricas;

import java.util.Arrays;

public enum Fase {
    APRENDIZADO(1, "Fase 1: Aprendizado", "Out-Dez/25",
            "O CPC inicial foi elevado (R$ 1,30 a R$ 1,59) devido ao uso de imagens estáticas e foco em vendas diretas. "
                    + "O algoritmo ainda estava a entender o perfil em Juiz de Fora (raio de 20km)."),
    VIRADA_DE_CHAVE(2, "Fase 2: Virada de Chave", "Jan-Abr/26",
            "A entrada do vídeo com a nova copy fez o CPC despencar 60% (R$ 0,64 em Jan/26) e o custo por conversa "
                    + "atingiu o mínimo histórico de R$ 7,31. Esta é a estrutura vencedora a ser replicada."),
    MANUTENCAO(3, "Fase 3: Manutenção", "Mai-Jul/26",
            "A redução do orçamento para R$ 20/dia enfraqueceu a entrega no leilão e elevou o CPC para R$ 1,10 - R$ 1,31. "
                    + "Orçamentos muito reduzidos provocam maior frequência e saturação local.");

    /** Análise exibida quando nenhuma fase é filtrada (texto do relatório original). */
    public static final String ANALISE_GERAL =
            "A conta gerou 1.007 oportunidades no WhatsApp a um custo médio de R$ 9,27/lead. O formato em vídeo provou ser "
                    + "o campeão indiscutível. A prioridade atual é rastrear as vendas no pós-WhatsApp.";

    private final int numero;
    private final String rotulo;
    private final String periodo;
    private final String analise;

    // guarda os dados fixos de cada fase
    Fase(int numero, String rotulo, String periodo, String analise) {
        this.numero = numero;
        this.rotulo = rotulo;
        this.periodo = periodo;
        this.analise = analise;
    }

    // número da fase (1 a 3), usado no banco e na URL
    public int numero() { return numero; }

    // rótulo para a interface
    public String rotulo() { return rotulo; }

    // período coberto pela fase
    public String periodo() { return periodo; }

    // texto de análise do especialista para a fase
    public String analise() { return analise; }

    // converte o número vindo do banco ou da URL; lança IllegalArgumentException se não existir
    public static Fase deNumero(int numero) {
        return Arrays.stream(values()).filter(f -> f.numero == numero).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Fase inexistente: " + numero));
    }
}
// fim de Fase.java
