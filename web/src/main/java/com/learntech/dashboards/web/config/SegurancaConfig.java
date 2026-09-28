// Segurança da web: páginas de leitura públicas; Kanban exige login (usuário e senha por variável de ambiente)
package com.learntech.dashboards.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SegurancaConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SegurancaConfig.class);

    // regras de acesso (matchers Ant explícitos porque há dois servlets: Faces e MVC); CSRF do Spring fica só no /login e /logout, pois o JSF já protege com ViewState no servidor
    @Bean
    SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(a -> a
                        .requestMatchers(antMatcher("/kanban.xhtml"), antMatcher("/kanban")).authenticated()
                        .anyRequest().permitAll())
                .formLogin(f -> f.defaultSuccessUrl("/kanban.xhtml", true))
                .logout(l -> l.logoutSuccessUrl("/index.xhtml"))
                .csrf(c -> c.ignoringRequestMatchers(antMatcher("/*.xhtml"), antMatcher("/jakarta.faces.resource/**")))
                .headers(h -> h.contentSecurityPolicy(csp -> csp.policyDirectives(
                        "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; "
                                + "style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:; "
                                + "frame-ancestors 'none'; form-action 'self'")));
        return http.build();
    }

    // usuário único do Kanban; sem KANBAN_SENHA, gera senha aleatória não divulgada (acesso bloqueado)
    @Bean
    UserDetailsService usuarios(@Value("${dashboards.kanban.usuario}") String usuario,
                                @Value("${dashboards.kanban.senha:}") String senha,
                                PasswordEncoder codificador) {
        String efetiva = senha;
        if (senha == null || senha.isBlank()) {
            efetiva = UUID.randomUUID().toString();
            LOG.warn("KANBAN_SENHA não definida: o Kanban fica bloqueado até configurar a variável.");
        }
        return new InMemoryUserDetailsManager(
                User.withUsername(usuario).password(codificador.encode(efetiva)).roles("GESTOR").build());
    }

    // codificador padrão (bcrypt)
    @Bean
    PasswordEncoder codificador() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
// fim de SegurancaConfig.java
