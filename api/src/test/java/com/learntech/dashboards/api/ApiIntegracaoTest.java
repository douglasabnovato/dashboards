// Testes de integração da API com H2 + Flyway: dados originais, simulador, WIP Limit e chave de API
package com.learntech.dashboards.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegracaoTest {

    private static final String CHAVE = "chave-de-teste";

    @Autowired
    private MockMvc mvc;

    // cada teste começa com o quadro original
    @BeforeEach
    void restaurarQuadro() throws Exception {
        mvc.perform(post("/api/kanban/reset").header("X-Api-Key", CHAVE)).andExpect(status().isNoContent());
    }

    // histórico completo: 10 meses, 1.007 conversas, CPA 9,27 e CPC ponderado 0,94
    @Test
    void painelGeral() throws Exception {
        mvc.perform(get("/api/metricas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meses.length()").value(10))
                .andExpect(jsonPath("$.meses[0].rotulo").value("Out/25"))
                .andExpect(jsonPath("$.resumo.conversasTotais").value(1007))
                .andExpect(jsonPath("$.resumo.cpaMedio").value(9.27))
                .andExpect(jsonPath("$.resumo.cpcMedio").value(0.94));
    }

    // filtro por fase e fase inválida
    @Test
    void painelPorFase() throws Exception {
        mvc.perform(get("/api/metricas").param("fase", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meses.length()").value(4))
                .andExpect(jsonPath("$.resumo.conversasTotais").value(518));
        mvc.perform(get("/api/metricas").param("fase", "7")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/metricas").param("fase", "abc")).andExpect(status().isBadRequest());
    }

    // simulador com o preset Retomada e com valor fora da faixa
    @Test
    void simulador() throws Exception {
        mvc.perform(get("/api/simulador/presets")).andExpect(jsonPath("$.length()").value(4));
        mvc.perform(post("/api/simulador").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orcamentoDiario\":50,\"cpa\":8,\"conversaoPercentual\":5,\"ticketMedio\":800}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roas").value(5.0))
                .andExpect(jsonPath("$.faixa").value("ESCALA_DE_OURO"));
        mvc.perform(post("/api/simulador").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orcamentoDiario\":5000,\"cpa\":8,\"conversaoPercentual\":5,\"ticketMedio\":800}"))
                .andExpect(status().isBadRequest());
    }

    // quadro original: 37 tarefas, 1 em Doing; filtro por BU e dia
    @Test
    void quadro() throws Exception {
        mvc.perform(get("/api/kanban").header("X-Api-Key", CHAVE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tarefas.length()").value(37))
                .andExpect(jsonPath("$.emAndamento").value(1))
                .andExpect(jsonPath("$.wipLimit").value(1));
        mvc.perform(get("/api/kanban").header("X-Api-Key", CHAVE).param("projeto", "MEDTREM").param("dia", "SEG"))
                .andExpect(jsonPath("$.tarefas.length()").value(1))
                .andExpect(jsonPath("$.tarefas[0].id").value("a1a"));
        mvc.perform(get("/api/kanban").header("X-Api-Key", CHAVE).param("projeto", "XPTO"))
                .andExpect(status().isBadRequest());
    }

    // Kanban sem chave ou com chave errada: 401 (leitura e escrita)
    @Test
    void kanbanExigeChave() throws Exception {
        mvc.perform(get("/api/kanban")).andExpect(status().isUnauthorized());
        mvc.perform(patch("/api/kanban/tarefas/a1a").contentType(MediaType.APPLICATION_JSON)
                .content("{\"etapa\":\"QA\"}")).andExpect(status().isUnauthorized());
        mvc.perform(patch("/api/kanban/tarefas/a1a").header("X-Api-Key", "errada")
                .contentType(MediaType.APPLICATION_JSON).content("{\"etapa\":\"QA\"}"))
                .andExpect(status().isUnauthorized());
    }

    // WIP=1: segunda tarefa em Doing é recusada; depois de liberar a coluna, é aceita
    @Test
    void wipLimit() throws Exception {
        mvc.perform(patch("/api/kanban/tarefas/a1a").header("X-Api-Key", CHAVE)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"etapa\":\"DOING\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.tarefaAtiva").value("b3a"));
        mvc.perform(patch("/api/kanban/tarefas/b3a").header("X-Api-Key", CHAVE)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"etapa\":\"QA\"}"))
                .andExpect(status().isOk());
        mvc.perform(patch("/api/kanban/tarefas/a1a").header("X-Api-Key", CHAVE)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"etapa\":\"DOING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etapa").value("DOING"));
        mvc.perform(patch("/api/kanban/tarefas/nao-existe").header("X-Api-Key", CHAVE)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"etapa\":\"QA\"}"))
                .andExpect(status().isNotFound());
    }
}
// fim de ApiIntegracaoTest.java
