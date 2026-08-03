package com.bryan.portafolioBackend.model;

/*
 * ============================================================
 *  CAPA MODEL (Entidad / Dominio)
 * ============================================================
 *  Responsabilidad: Representar las tablas de la base de datos
 *  como objetos Java (POJOs/JPA Entities).
 *
 *  - Define columnas, claves primarias, relaciones y constraints.
 *  - Solo guarda estado/datos, sin lógica de negocio compleja.
 *  - Spring/JPA se encarga de mapearla a la tabla correspondiente.
 * ============================================================
 */

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Schema(description="Entidad que presenta un proyecto del portafolio")
public class Project {

    @Id // Esta es la clave primaria
    @GeneratedValue(strategy = GenerationType.UUID) // Autogenera un UUID único
    @Schema(description = "ID unico autogenerado del proyecto")
    private UUID id;
    @Schema(description = "Título del proyecto")
    private String title;

    @Column(columnDefinition = "TEXT") // TEXT para que quepan descripciones largas
    @Schema(description = "Descripción del proyecto")
    private String description;

    @Schema(description = "URL de la imagen del proyecto")
    private String imageUrl;
    @Schema(description = "URL del proyecto en vivo")
    private String liveUrl;
    @Schema(description = "URL del código fuente en GitHub")
    private String githubUrl;

    @ElementCollection // esta sirve para indicar que techStack es una colección de elementos simples (Strings) y no una entidad separada
    @Schema(description = "Pila tecnológica del proyecto")//sirve para documentar en Swagger/OpenAP
    private List<String> techStack;

    @CreationTimestamp // Guarda automáticamente la fecha y hora de creación
    @Schema(description = "Fecha y hora de creación del proyecto")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Categoría del proyecto")
    private ProjectCategory category;


}