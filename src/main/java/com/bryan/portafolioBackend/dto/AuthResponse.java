package com.bryan.portafolioBackend.dto; // Ajusta a tu paquete

import lombok.Data;

@Data
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}