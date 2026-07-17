package com.bryan.portafolioBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Le dice a Spring:Lee esta configuración al arrancar
public class CorsConfig {

    @Bean //un Bean es un objeto que Spring gestiona y que podemos inyectar en cualquier parte de nuestra aplicación
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Aplicamos esta regla a todas las rutas que empiecen por /api/
                        .allowedOrigins("http://localhost:3000") // Permitimos que Next.js nos llame
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Qué cosas puede hacer
                        .allowedHeaders("*") // Permitimos que nos envíe cualquier cabecera (súper importante para que pase el Authorization: Bearer...)
                        .allowCredentials(true);
            }
        };
    }
}