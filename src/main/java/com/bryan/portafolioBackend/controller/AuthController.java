package com.bryan.portafolioBackend.controller;

/*
 * ============================================================
 *  CAPA CONTROLLER (Controlador / API REST)
 * ============================================================
 *  Responsabilidad: Recibir las peticiones HTTP del exterior
 *  y devolver las respuestas HTTP correspondientes.
 *
 *  - Mapea URLs a métodos (@GetMapping, @PostMapping...).
 *  - Extrae datos del request (@RequestBody, @RequestParam).
 *  - Delega TODO el trabajo a la capa Service (nunca toca BD directamente).
 *  - NO debe tener lógica de negocio, solo orquestación.
 * ============================================================
 */

import com.bryan.portafolioBackend.dto.AuthRequest;
import com.bryan.portafolioBackend.dto.AuthResponse;
import com.bryan.portafolioBackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para iniciar sesión y obtener token JWT")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
