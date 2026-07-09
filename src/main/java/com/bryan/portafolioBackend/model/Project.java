package com.bryan.portafolioBackend.model;

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

    @ElementCollection // Le dice a Spring que esto es una lista de textos simples
    private List<String> techStack;

    @CreationTimestamp // Guarda automáticamente la fecha y hora de creación
    private LocalDateTime createdAt;


}