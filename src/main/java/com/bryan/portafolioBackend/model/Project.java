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

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity // Le dice a Spring que esto es una tabla de base de datos
@Table(name = "projects") // El nombre que tendrá la tabla en PostgreSQL
@Data // Magia de Lombok: nos crea los getters y setters automáticamente
public class Project {

    @Id // Esta es la clave primaria
    @GeneratedValue(strategy = GenerationType.UUID) // Autogenera un UUID único
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT") // TEXT para que quepan descripciones largas
    private String description;

    private String imageUrl;
    private String liveUrl;
    private String githubUrl;

    @ElementCollection // esta sirve para indicar que techStack es una colección de elementos simples (Strings) y no una entidad separada
    private List<String> techStack;

    @CreationTimestamp // Guarda automáticamente la fecha y hora de creación
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectCategory category;


}