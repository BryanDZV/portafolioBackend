package com.bryan.portafolioBackend.dto; // Ajusta a tu paquete

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}