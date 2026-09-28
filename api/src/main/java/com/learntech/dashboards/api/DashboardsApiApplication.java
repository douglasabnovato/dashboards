// Ponto de entrada da API REST do learnTECH OS (métricas, simulador e Kanban)
package com.learntech.dashboards.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DashboardsApiApplication {

    // sobe o servidor HTTP
    public static void main(String[] args) {
        SpringApplication.run(DashboardsApiApplication.class, args);
    }
}
// fim de DashboardsApiApplication.java
