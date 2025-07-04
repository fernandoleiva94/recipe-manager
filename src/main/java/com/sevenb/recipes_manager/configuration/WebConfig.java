package com.sevenb.recipes_manager.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${recipe.manager.service.recipeFrontUrl}")
    private String recipeFrontUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Ajusta la ruta según sea necesario
                .allowedOrigins(recipeFrontUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}