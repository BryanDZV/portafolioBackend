package com.bryan.portafolioBackend.exception; // Asegúrate de que coincida con tu proyecto

/*
 * ============================================================
 *  CAPA EXCEPTION (Manejo global de errores)
 * ============================================================
 *  Responsabilidad: Capturar y transformar excepciones
 *  en respuestas HTTP limpias y amigables para el cliente.
 *
 *  - Usa @ControllerAdvice para vigilar todos los controllers.
 *  - Centraliza el tratamiento de errores (validaciones, faltantes, etc.).
 *  - Evita que stacktraces crudos lleguen al frontend.
 * ============================================================
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice//Esta es la anotacion magica que le dice a Spring: Vigila todos los controladores
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //-------- Manejo de Errores de Validacion (Formularios y JSON)
    // 1. Atrapa errores de validacion cuando nos envian JSON puro (@RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(extractFieldErrors(ex.getBindingResult()));
    }

    // 2. Atrapa errores de validacion cuando nos envian Form-Data (@ModelAttribute)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindExceptions(BindException ex) {
        return ResponseEntity.badRequest().body(extractFieldErrors(ex.getBindingResult()));
    }

    // 3. Atrapa el error cuando se olvidan de enviar un archivo obligatorio (como la imagen en el POST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingPart(MissingServletRequestPartException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Falta el archivo obligatorio: " + ex.getRequestPartName());
        return ResponseEntity.badRequest().body(error);
    }

    // 3.5 Atrapa el error cuando el archivo excede el tamaño permitido (imagen muy pesada)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex, WebRequest request) {
        log.warn("Archivo demasiado grande: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, "Payload Too Large",
                "El archivo excede el tamaño máximo permitido (10MB).", request);
    }

    // -----Manejo de Errores de Negocio y Servidor
    //4. Atrapa errores de validacion de negocio (categoria invalida, datos faltantes, etc.) - 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
    }

    //5. Atrapa errores de credenciales incorrectas en el login - 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), request);
    }

    //6. Atrapa errores de entrada/salida (ej: fallo al subir imagen a Cloudinary) - 500
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(
            IOException ex, WebRequest request) {
        log.error("Error al procesar archivo: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Error al procesar el archivo.", request);
    }

    //7. Atrapa errores cuando buscamos id que no existe 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    //8. Atrapa CUALQUIER otro error imprevisto para que la app no crashee feo (Error 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Ha ocurrido un error inesperado en el servidor.", request);
    }

    // ----- Metodos auxiliares privados

    // Extrae los errores de validacion de un BindingResult y los devuelve como mapa campo -> mensaje
    private Map<String, String> extractFieldErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    // Construye un ResponseEntity<ErrorResponse> usando el factory method de ErrorResponse
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error,
                                                              String message, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        ErrorResponse body = ErrorResponse.of(status.value(), error, message, path);
        return new ResponseEntity<>(body, status);
    }

}
