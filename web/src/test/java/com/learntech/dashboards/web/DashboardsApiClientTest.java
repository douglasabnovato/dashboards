// Teste do cliente HTTP contra um servidor real (JDK HttpServer): PATCH, 409 do WIP e chave de API
package com.learntech.dashboards.web;

import com.learntech.dashboards.core.kanban.Etapa;
import com.learntech.dashboards.core.kanban.WipExcedidoException;
import com.learntech.dashboards.web.api.ApiIndisponivelException;
import com.learntech.dashboards.web.api.DashboardsApiClient;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DashboardsApiClientTest {

    private HttpServer servidor;
    private final AtomicReference<String> metodo = new AtomicReference<>();
    private final AtomicReference<String> chave = new AtomicReference<>();

    // sobe um servidor local que responde 409 ao PATCH, como a API faz quando o WIP está cheio
    @BeforeEach
    void subir() throws IOException {
        servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        servidor.createContext("/api/kanban/tarefas/a1a", troca -> {
            metodo.set(troca.getRequestMethod());
            chave.set(troca.getRequestHeaders().getFirst("X-Api-Key"));
            byte[] corpo = "{\"status\":409,\"tarefaAtiva\":\"b3a\"}".getBytes(StandardCharsets.UTF_8);
            troca.getResponseHeaders().add("Content-Type", "application/problem+json");
            troca.sendResponseHeaders(409, corpo.length);
            troca.getResponseBody().write(corpo);
            troca.close();
        });
        servidor.start();
    }

    // encerra o servidor
    @AfterEach
    void parar() {
        servidor.stop(0);
    }

    // o PATCH chega ao servidor com a chave e o 409 vira WipExcedidoException
    @Test
    void patchComWip() {
        DashboardsApiClient cliente = new DashboardsApiClient(RestClient.builder(),
                "http://127.0.0.1:" + servidor.getAddress().getPort(), "segredo");
        WipExcedidoException e = assertThrows(WipExcedidoException.class, () -> cliente.mover("a1a", Etapa.DOING));
        assertEquals("b3a", e.tarefaAtiva());
        assertEquals("PATCH", metodo.get());
        assertEquals("segredo", chave.get());
    }

    // porta fechada vira ApiIndisponivelException
    @Test
    void apiForaDoAr() {
        DashboardsApiClient cliente = new DashboardsApiClient(RestClient.builder(), "http://127.0.0.1:9", "");
        assertThrows(ApiIndisponivelException.class, () -> cliente.painel(null));
    }
}
// fim de DashboardsApiClientTest.java
