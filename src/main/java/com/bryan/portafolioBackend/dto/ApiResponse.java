package com.bryan.portafolioBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor sirve para generar automáticamente un constructor sin argumentos
@NoArgsConstructor
//@AllArgsConstructor sirve para generar automáticamente un constructor con todos los argumentos
@AllArgsConstructor
public class ApiResponse {
    private String message;
}
