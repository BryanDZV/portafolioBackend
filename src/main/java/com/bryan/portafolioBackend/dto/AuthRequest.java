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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud de autenticación")
public class AuthRequest {
    @Schema(description = "Correo electrónico del usuario", example = "admin@ejemplo.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "miContraseña123")
    private String password;
}