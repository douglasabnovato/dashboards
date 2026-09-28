// Protege todas as rotas do Kanban (/api/kanban, leitura e escrita) com o cabeçalho X-Api-Key
package com.learntech.dashboards.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class ChaveApiFilter extends OncePerRequestFilter {

    public static final String CABECALHO = "X-Api-Key";

    private final byte[] chave;

    // lê a chave da variável DASHBOARDS_API_KEY; vazia = Kanban bloqueado (falha fechada)
    public ChaveApiFilter(@Value("${dashboards.api-key:}") String chave) {
        this.chave = chave.getBytes(StandardCharsets.UTF_8);
    }

    // só filtra o Kanban (dados internos da operação); métricas e simulador são públicos
    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        return !req.getRequestURI().startsWith("/api/kanban");
    }

    // compara a chave em tempo constante e responde 401 em JSON quando não confere
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String enviada = req.getHeader(CABECALHO);
        boolean valida = chave.length > 0 && enviada != null
                && MessageDigest.isEqual(chave, enviada.getBytes(StandardCharsets.UTF_8));
        if (!valida) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{\"status\":401,\"title\":\"Não autorizado\",\"detail\":\"Envie o cabeçalho X-Api-Key.\"}");
            return;
        }
        chain.doFilter(req, res);
    }
}
// fim de ChaveApiFilter.java
