// Redireciona a raiz do site para a página inicial do hub
package com.learntech.dashboards.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // "/" e "/kanban" levam às páginas JSF correspondentes
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index.xhtml");
        registry.addRedirectViewController("/kanban", "/kanban.xhtml");
    }
}
// fim de WebConfig.java
