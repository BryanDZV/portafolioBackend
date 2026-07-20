package com.bryan.portafolioBackend.config;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Le dice a Spring: Lee esta configuración al arrancar
public class CorsConfig {

    // Inyectamos la variable desde el entorno (en Render la configuraremos)
    // Si no encuentra la variable, usamos localhost:3000 como valor por defecto
    @Value("${ALLOWED_ORIGIN:http://localhost:3000}")//asignamos un valor por defecto en caso de que no se encuentre la variable de entorno
    private String allowedOrigin;

    @Bean // Un Bean es un objeto que Spring gestiona y que podemos inyectar en cualquier parte de nuestra aplicación
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nullable CorsRegistry registry) {
                // Aplicamos esta regla a todas las rutas que empiecen por /api/
                registry.addMapping("/api/**")
                        // Permitimos tanto localhost como la URL de producción que viene del entorno
                        .allowedOrigins("http://localhost:3000", allowedOrigin)
                        // Qué métodos HTTP permitimos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Permitimos cualquier cabecera (fundamental para el token de autorización)
                        .allowedHeaders("*")
                        // Permitimos el envío de cookies/credenciales
                        .allowCredentials(true);
            }
        };
    }
}