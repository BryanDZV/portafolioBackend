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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
