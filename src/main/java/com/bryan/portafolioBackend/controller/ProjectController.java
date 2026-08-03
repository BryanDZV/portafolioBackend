package com.bryan.portafolioBackend.controller;

import com.bryan.portafolioBackend.dto.ApiResponse;
import com.bryan.portafolioBackend.dto.ProjectRequest;
import com.bryan.portafolioBackend.model.Project;

import com.bryan.portafolioBackend.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Proyectos", description = "Gestión de proyectos del portafolio")//el tag sirve para agrupar y categorizar los endpoints relacionados con la gestión de proyectos en la documentación de Swagger/OpenAPI.
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Obtener todos los proyectos", description = "Devuelve una lista de todos los proyectos publicados")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de proyectos obtenida exitosamente")
    @GetMapping
    public List<Project> getProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Crear un nuevo proyecto", description = "Crea un proyecto con imagen asociada")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Proyecto creado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ApiResponse> createProject(
            @Valid @ModelAttribute ProjectRequest request,
            @Parameter(description = "Imagen del proyecto") @RequestParam("image") MultipartFile image) throws IOException {

        // El controlador solo delega al servicio
        projectService.createProject(request, image);
        return ResponseEntity.ok(new ApiResponse("Proyecto creado exitosamente"));
    }

    @Operation(summary = "Eliminar un proyecto", description = "Elimina un proyecto por su ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Proyecto eliminado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProject(
            @Parameter(description = "ID único del proyecto") @PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new ApiResponse("Proyecto Eliminado Correctamente"));
    }

    @Operation(summary = "Actualizar un proyecto", description = "Actualiza un proyecto existente (imagen opcional)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Proyecto actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProject(
            @Parameter(description = "ID único del proyecto") @PathVariable UUID id,
            @Valid @ModelAttribute ProjectRequest request,
            @Parameter(description = "Nueva imagen del proyecto (opcional)") @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        projectService.updateProject(id, request, image);
        return ResponseEntity.ok(new ApiResponse("Proyecto actualizado exitosamente"));
    }
}