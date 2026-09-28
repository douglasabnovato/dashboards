// Teste de fumaça das páginas JSF com a API apontando para uma porta fechada
package com.learntech.dashboards.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"dashboards.api.base-url=http://127.0.0.1:9", "dashboards.kanban.senha=senha-de-teste"})
class PaginasTest {

    @Autowired
    private TestRestTemplate http;

    // hub abre com os quatro módulos
    @Test
    void hub() {
        ResponseEntity<String> r = http.getForEntity("/index.xhtml", String.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertTrue(r.getBody().contains("Growth Hub"));
        assertTrue(r.getBody().contains("lang=\"pt-BR\""));
    }

    // analytics com API fora do ar mostra aviso amigável (sem 500)
    @Test
    void analyticsSemApi() {
        ResponseEntity<String> r = http.getForEntity("/analytics.xhtml", String.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertTrue(r.getBody().contains("Não foi possível carregar os dados agora"));
    }

    // Kanban exige login
    @Test
    void kanbanExigeLogin() {
        ResponseEntity<String> r = http.getForEntity("/kanban.xhtml", String.class);
        boolean redirecionou = r.getStatusCode().is3xxRedirection()
                && r.getHeaders().getLocation() != null
                && r.getHeaders().getLocation().getPath().equals("/login");
        boolean mostrouLogin = r.getBody() != null && r.getBody().contains("name=\"username\"");
        assertTrue(redirecionou || mostrouLogin);
    }
}
// fim de PaginasTest.java
