package com.bryan.portafolioBackend.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component // Con esto Spring sabe que este filtro existe y lo aplica automáticamente a todas las peticiones
public class RateLimitingFilter extends OncePerRequestFilter {

    // Aquí guardaremos la IP del cliente y su "cubo" de fichas asociado
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Método para fabricar un cubo nuevo para los visitantes nuevos
    private Bucket createNewBucket() {
        // Configuramos el límite: 10 peticiones de capacidad, recargando 10 fichas cada 1 minuto
        Bandwidth limit = Bandwidth.builder()
                .capacity(10)
                .refillGreedy(10, Duration.ofMinutes(1))
                .build();

        return Bucket.builder().addLimit(limit).build();
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtenemos la IP de quien hace la petición
        String ip = request.getRemoteAddr();

        // 2. Buscamos su cubo. Si es la primera vez que entra, le creamos uno nuevo.
        Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());

        // 3. Intentamos consumir 1 ficha.
        if (bucket.tryConsume(1)) {
            // ¡Tiene fichas! Le dejamos pasar hacia los Controllers o la Seguridad
            filterChain.doFilter(request, response);
        } else {
            // ¡Se quedó sin fichas! Le bloqueamos el paso y devolvemos 429
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Has superado el limite de peticiones. Espera un minuto.\"}");
        }
    }
}
