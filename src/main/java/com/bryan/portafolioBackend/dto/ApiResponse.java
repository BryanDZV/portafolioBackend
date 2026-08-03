package com.bryan.portafolioBackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor sirve para generar automáticamente un constructor sin argumentos
@NoArgsConstructor
//@AllArgsConstructor sirve para generar automáticamente un constructor con todos los argumentos
@AllArgsConstructor
@Schema(description = "Respuesta genérica de la API")
public class ApiResponse {
    @Schema(description = "Mensaje de la respuesta", example = "Proyecto creado exitosamente")
    private String message;
}
