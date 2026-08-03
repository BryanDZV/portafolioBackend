package com.bryan.portafolioBackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para crear o actualizar un proyecto")
public class ProjectRequest {
    //la anotación @Size sirve para validar que el tamaño del campo esté entre 3 y 100 caracteres
    //sus argumentos son el mensaje que se mostrará si la validación falla
    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(min=3,max=100, message="El nombre del proyecto debe tener entre 3 y 100 caracteres")
    @Schema(description = "Título del proyecto", example = "Mi Portafolio Web")
    private String title;

    //@NotBlank sirve para validar que un campo no esté vacío y
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max=500, message="La descripción no puede superar los 500 caracteres")
    @Schema(description = "Descripción del proyecto", example = "Un portafolio web moderno con Spring Boot")
    private String description;

    // String de textos para las tecnologías (ej: ["React", "Spring Boot", "Docker"])
    @Schema(description = "Stack de tecnologías usadas", example = "React, Spring Boot, PostgreSQL")
    private String techStack;

    // Las URLs pueden ser nulas, así que no les ponemos @NotBlank
    @Schema(description = "URL del proyecto en vivo", example = "https://miportafolio.com")
    private String liveUrl;

    @Schema(description = "URL del repositorio en GitHub", example = "https://github.com/bryan/portafolio")
    private String githubUrl;

    // Para la Categoria, usamos un String que luego convertiremos a ProjectCategory en el servicio
    @Schema(description = "Categoría del proyecto", example = "WEB")
    private String category;

}
