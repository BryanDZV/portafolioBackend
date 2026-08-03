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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //csrf necesario cuando usamos cookies, pero como vamos a usar JWT lo desactivamos
                .csrf(AbstractHttpConfigurer::disable)
                //CON esta configuracion le decimos a spring que no guarde la sesion del usuario, ya que vamos a usar JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Las rutas de Swagger abiertas con el guardia vigilando (permitAll)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 2. Tus rutas públicas
                        .requestMatchers(HttpMethod.GET, "/api/projects").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/health").permitAll() //para mantener vivo el back en render
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