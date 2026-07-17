package com.bryan.portafolioBackend.config; // Ajusta a tu paquete

/*
 * ============================================================
 *  CAPA CONFIG (Configuración de beans y frameworks)
 * ============================================================
 *  Responsabilidad: Ajustar y conectar librerías de Spring
 *  (Security, CORS, Cloudinary, bases de datos, etc.).
 *
 *  - Crea @Beans que otros componentes inyectan (@Autowired).
 *  - Define reglas globales (cors, seguridad, filtros).
 *  - NO contiene lógica de negocio de la aplicación.
 * ============================================================
 */

import com.bryan.portafolioBackend.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

//este archivo permite extraer el token de la peticion del encabezado y validar si es correcto
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; //  INYECTAMOS NUESTRO NUEVO FILTRO

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/projects").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Le decimos a Spring que ponga nuestro filtro ANTES que el suyo por defecto
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}