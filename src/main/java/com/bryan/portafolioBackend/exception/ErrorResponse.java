package com.bryan.portafolioBackend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp; //cuando ocurrio el error
    private int status;// numero del error (tipo)
    private String error;//mensaje para el error
    private String message;//mensaje a mostrar
    private String path;//ruta

    // Metodo factory para construir rapidamente un ErrorResponse sin repetir tanto codigo en el GlobalExceptionHandler
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
}
