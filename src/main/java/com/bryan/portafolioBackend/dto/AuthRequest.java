package com.bryan.portafolioBackend.dto; // Ajusta a tu paquete

/*
 * ============================================================
 *  CAPA DTO (Data Transfer Object)
 * ============================================================
 *  Responsabilidad: Transportar datos entre el cliente (frontend/Postman)
 *  y el servidor, o entre capas internas del backend.
 *
 *  - NO contiene lógica de negocio.
 *  - Solo define qué campos viajan en las peticiones/respuestas.
 *  - Se usan para no exponer directamente las entidades del Modelo/BD.
 * ============================================================
 */

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}