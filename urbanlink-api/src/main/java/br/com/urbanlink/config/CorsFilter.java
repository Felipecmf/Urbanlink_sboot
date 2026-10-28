package br.com.urbanlink.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Permite que o front-end (React/Vercel) consuma a API.
 * As origens permitidas vêm da variável de ambiente CORS_ORIGIN (separadas por vírgula).
 */
@Component
public class CorsFilter implements Filter {

    private ArrayList<String> origensPermitidas = new ArrayList<>();

    public CorsFilter() {
        String origens = System.getenv("CORS_ORIGIN");
        if (origens == null || origens.isEmpty()) {
            origens = "http://localhost:5173";
        }
        for (String origem : origens.split(",")) {
            if (!origem.trim().isEmpty()) {
                origensPermitidas.add(origem.trim());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest requisicao = (HttpServletRequest) req;
        HttpServletResponse resposta = (HttpServletResponse) res;

        String origem = requisicao.getHeader("Origin");
        if (origensPermitidas.contains("*")) {
            resposta.setHeader("Access-Control-Allow-Origin", "*");
        } else if (origem != null && origensPermitidas.contains(origem)) {
            resposta.setHeader("Access-Control-Allow-Origin", origem);
            resposta.setHeader("Vary", "Origin");
        }
        resposta.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resposta.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resposta.setHeader("Access-Control-Max-Age", "3600");

        if ("OPTIONS".equalsIgnoreCase(requisicao.getMethod())) {   // pré-voo (preflight)
            resposta.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(req, res);
    }
}
