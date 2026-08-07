package com.bryan.portafolioBackend.security;

import com.bryan.portafolioBackend.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component // Con esto Spring sabe que este filtro existe y lo aplica automaticamente a todas las peticiones
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Aqui guardaremos la IP del cliente y su "cubo" de fichas asociado
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Metodo para fabricar un cubo nuevo para los visitantes nuevos
    private Bucket createNewBucket() {
        // Configuramos el limite: 10 peticiones de capacidad, recargando 10 fichas cada 1 minuto
        Bandwidth limit = Bandwidth.builder()
                .capacity(10)
                .refillGreedy(10, Duration.ofMinutes(1))
                .build();

        return Bucket.builder().addLimit(limit).build();
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtenemos la IP de quien hace la peticion
        String ip = request.getRemoteAddr();

        // 2. Buscamos su cubo. Si es la primera vez que entra, le creamos uno nuevo.
        Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());

        // 3. Intentamos consumir 1 ficha.
        if (bucket.tryConsume(1)) {
            // Tiene fichas! Le dejamos pasar hacia los Controllers o la Seguridad
            filterChain.doFilter(request, response);
        } else {
            // Se quedo sin fichas! Le bloqueamos el paso y devolvemos 429
            log.warn("Rate limit excedido para IP: {}", ip);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorResponse error = ErrorResponse.of(
                    HttpStatus.TOO_MANY_REQUESTS.value(),
                    "Too Many Requests",
                    "Has superado el limite de peticiones. Espera un minuto.",
                    request.getRequestURI()
            );
            objectMapper.writeValue(response.getOutputStream(), error);
        }
    }
}
