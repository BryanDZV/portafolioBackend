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

import com.bryan.portafolioBackend.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Buscamos la cabecera "Authorization" en la peticion de Postman/Frontend
        String authHeader = request.getHeader("Authorization");

        // 2. Comprobamos si nos enviaron un token y si empieza por "Bearer " (el estandar de la industria)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Quitamos la palabra "Bearer " para quedarnos solo con el codigo

            // 3. Si el token es valido...
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);

                // Creamos un "pase VIP" y lo guardamos para que Spring sepa que estas autorizado
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // Token presente pero invalido o expirado: devolvemos 401 con formato ErrorResponse
                log.warn("Token JWT invalido o expirado desde IP: {}", request.getRemoteAddr());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ErrorResponse error = ErrorResponse.of(
                        HttpStatus.UNAUTHORIZED.value(),
                        "Unauthorized",
                        "Token invalido o expirado",
                        request.getRequestURI()
                );
                objectMapper.writeValue(response.getOutputStream(), error);
                return;
            }
        }

        // 4. Continuamos la peticion normalmente
        chain.doFilter(request, response);
    }
}