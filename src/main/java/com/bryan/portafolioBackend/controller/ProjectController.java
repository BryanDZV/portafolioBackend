package com.bryan.portafolioBackend.controller;

import com.bryan.portafolioBackend.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse> createProject(
            @Valid @ModelAttribute ProjectRequest request,
            @RequestParam("image") MultipartFile image) throws IOException {

        // El controlador solo delega al servicio
        projectService.createProject(request, image);

        return ResponseEntity.ok(new ApiResponse("Proyecto creado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new ApiResponse("Proyecto Eliminado Correctamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProject(
            @PathVariable UUID id,
            @Valid @ModelAttribute ProjectRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        projectService.updateProject(id, request, image);
        return ResponseEntity.ok(new ApiResponse("Proyecto actualizado exitosamente"));
    }
}