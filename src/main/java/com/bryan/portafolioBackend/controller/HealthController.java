package com.bryan.portafolioBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //asi decimos que es un controlador
@RequestMapping("/api")//ruta de url
@Tag(name = "Health", description = "Endpoint para verificar que el backend está vivo")
public class HealthController {

    @Operation(summary = "Health Check", description = "Verifica si el backend está activo y funcionando")
    @ApiResponse(responseCode = "200", description = "Backend activo")
    @GetMapping("/health")//ruta de irl
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend activo y funcionando");
    }
}