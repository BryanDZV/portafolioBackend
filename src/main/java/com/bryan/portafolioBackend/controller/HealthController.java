package com.bryan.portafolioBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //asi decimos que es un controlador
@RequestMapping("/api")//ruta de url
public class HealthController {

    @GetMapping("/health")//ruta de irl
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Backend activo y funcionando");
    }
}