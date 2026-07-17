package com.bryan.portafolioBackend.repository;

/*
 * ============================================================
 *  CAPA REPOSITORY (Persistencia / Acceso a datos)
 * ============================================================
 *  Responsabilidad: Comunicarse directamente con la base de datos.
 *
 *  - Extiende JpaRepository para obtener CRUD gratis (save, findAll, etc.).
 *  - Define consultas personalizadas si se necesitan.
 *  - Es la única capa que toca la BD; Service la utiliza.
 * ============================================================
 */

import com.bryan.portafolioBackend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

// Al extender de JpaRepository, Spring nos regala gratis todos los métodos CRUD (save, delete, findAll, findById...)
public interface ProjectRepository extends JpaRepository<Project, UUID> {
}