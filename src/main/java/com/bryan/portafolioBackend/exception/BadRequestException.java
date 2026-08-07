package com.bryan.portafolioBackend.exception;

//Para errores de negocio como categorías inválidas, datos faltantes o mal formados, etc.
//se usa en el service cuando los datos enviados por el cliente no son válidos según las reglas de negocio
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
