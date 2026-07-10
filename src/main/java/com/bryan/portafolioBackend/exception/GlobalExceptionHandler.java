package com.bryan.portafolioBackend.exception; // Asegúrate de que coincida con tu proyecto

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice//Esta es la anotación mágica que le dice a Spring: Vigila todos los controladores
public class GlobalExceptionHandler {

    // 1. Atrapa errores de validación cuando nos envían JSON puro (@RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        //creamo un mapa de errores donde la clave es el nombre del campo y el valor es el mensaje de error
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // 2. Atrapa errores de validación cuando nos envían Form-Data (@ModelAttribute)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindExceptions(BindException ex) {
        //creamo un mapa de errores donde la clave es el nombre del campo y el valor es el mensaje de error
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                //el mensaje se saca del error y se pone en el mapa con la clave siendo el nombre del campo
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // 3. Atrapa el error cuando se olvidan de enviar un archivo obligatorio (como la imagen en el POST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingPart(MissingServletRequestPartException ex) {
        //creamo un mapa de errores donde la clave es el nombre del campo y el valor es el mensaje de error
        Map<String, String> error = new HashMap<>();
        error.put("error", "Falta el archivo obligatorio: " + ex.getRequestPartName());
        return ResponseEntity.badRequest().body(error);
    }
}