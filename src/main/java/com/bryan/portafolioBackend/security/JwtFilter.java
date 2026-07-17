package com.bryan.portafolioBackend.security; // Ajusta a tu paquete

/*
 * ============================================================
 *  CAPA SECURITY (Seguridad / Autenticación y Autorización)
 * ============================================================
 *  Responsabilidad: Proteger los endpoints y validar identidad.
 *
 *  - Filtros: interceptan peticiones para validar tokens (JWT).
 *  - Utilidades: generan/verifican tokens y credenciales.
 *  - Configuración: define qué rutas son públicas y cuáles privadas.
 * ============================================================
 */

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Buscamos la cabecera "Authorization" en la petición de Postman/Frontend
        String authHeader = request.getHeader("Authorization");

        // 2. Comprobamos si nos enviaron un token y si empieza por "Bearer " (el estándar de la industria)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Quitamos la palabra "Bearer " para quedarnos solo con el código

            // 3. Si el token es válido...
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);

                // Creamos un "pase VIP" y lo guardamos para que Spring sepa que estás autorizado
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4. Continuamos la petición normalmente
        chain.doFilter(request, response);
    }
}