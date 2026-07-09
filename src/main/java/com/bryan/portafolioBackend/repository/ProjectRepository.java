package com.bryan.portafolioBackend.repository;

import com.bryan.portafolioBackend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

// Al extender de JpaRepository, Spring nos regala gratis todos los métodos CRUD (save, delete, findAll, findById...)
public interface ProjectRepository extends JpaRepository<Project, UUID> {
}