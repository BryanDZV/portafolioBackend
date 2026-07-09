package com.bryan.portafolioBackend.controller;

import com.bryan.portafolioBackend.model.Project;
import com.bryan.portafolioBackend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
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
            @RequestParam("image") MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("description") String description) throws IOException {
        Project savedProject = projectService.createProject(image, title, description);
        return ResponseEntity.ok(savedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable UUID id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("description") String description) throws IOException {
        Project updatedProject = projectService.updateProject(id, image, title, description);
        return ResponseEntity.ok(updatedProject);
    }
}
