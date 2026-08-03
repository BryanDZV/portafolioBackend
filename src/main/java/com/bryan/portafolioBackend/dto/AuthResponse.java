package com.bryan.portafolioBackend.dto; // Ajusta a tu paquete

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta de autenticación exitosa")
public class AuthResponse {
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}