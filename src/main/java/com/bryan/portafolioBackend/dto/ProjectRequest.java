package com.bryan.portafolioBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class ProjectRequest {
    //la anotación @Size sirve para validar que el tamaño del campo esté entre 3 y 100 caracteres
    //sus argumentos son el mensaje que se mostrará si la validación falla
    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(min=3,max=100, message="El nombre del proyecto debe tener entre 3 y 100 caracteres")
    private String title;
    //@NotBlank sirve para validar que un campo no esté vacío y
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max=500, message="La descripción no puede superar los 500 caracteres")
    private String description;

    // String de textos para las tecnologías (ej: ["React", "Spring Boot", "Docker"])
    private String techStack;

    // Las URLs pueden ser nulas, así que no les ponemos @NotBlank
    private String liveUrl;
    private String githubUrl;

    // Para la Categoria, usamos un String que luego convertiremos a ProjectCategory en el servicio
    private String category;

}
