package com.bryan.portafolioBackend.controller;

import com.bryan.portafolioBackend.dto.ProjectRequest;
import com.bryan.portafolioBackend.model.Project;

import com.bryan.portafolioBackend.service.ProjectService;
import jakarta.validation.Valid; // Importa esto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @GetMapping
    public List<Project> getProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @Valid @ModelAttribute ProjectRequest request,
            @RequestParam("image") MultipartFile image) throws IOException {

        // El controlador solo delega al servicio
        Project savedProject = projectService.createProject(request, image);

        return ResponseEntity.ok(savedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    // El PUT lo ajustaremos en el siguiente paso para que también use el DTO
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable UUID id,
            @Valid @ModelAttribute ProjectRequest request, // Usamos DTO aquí también
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Project updatedProject = projectService.updateProject(id, request, image);
        return ResponseEntity.ok(updatedProject);
    }
}