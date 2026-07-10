package com.bryan.portafolioBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data //sirve para generar automáticamente los métodos getters, setters, toString, equals y hashCode

public class ProjectRequest {
    //esta anotacion sirve para validar que el campo no esté vacío y la anotación @Size sirve para validar que el tamaño del campo esté entre 3 y 100 caracteres
    //sus argumentos son el mensaje que se mostrará si la validación falla
    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(min=3,max=100, message="El nombre del proyecto debe tener entre 3 y 100 caracteres")
    private String title;
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max=500, message="La descripción no puede superar los 500 caracteres")
    private String description;
}
