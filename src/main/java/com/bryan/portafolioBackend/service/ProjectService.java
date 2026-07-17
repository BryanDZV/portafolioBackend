package com.bryan.portafolioBackend.service;

/*
 * ============================================================
 *  CAPA SERVICE (Lógica de negocio)
 * ============================================================
 *  Responsabilidad: Ejecutar las reglas y operaciones del negocio.
 *
 *  - Recibe órdenes del Controller y orquesta el trabajo.
 *  - Habla con Repository para guardar/buscar en la BD.
 *  - Habla con otras APIs/servicios (Cloudinary, email, etc.).
 *  - Transforma y valida datos antes de persistirlos.
 *  - NO atiende peticiones HTTP directamente (eso es Controller).
 * ============================================================
 */

import com.bryan.portafolioBackend.dto.ProjectRequest;
import com.bryan.portafolioBackend.model.Project;
import com.bryan.portafolioBackend.model.ProjectCategory;
import com.bryan.portafolioBackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//este archivo es el que contiene la lógica de negocio, es decir,
//las operaciones que se pueden realizar sobre los proyectos,
// como crear, actualizar, eliminar y obtener proyectos.
// Se comunica con el repositorio para acceder a la base de datos
// y con el servicio de Cloudinary para gestionar las imágenes.
//también se encarga de normalizar la información recibida en las solicitudes, como el techStack, para asegurarse de que los datos almacenados sean consistentes y estén en el formato correcto.
public class ProjectService {

    @Autowired //esta sirve para inyectar la dependencia de ProjectRepository en ProjectService, lo que permite acceder a la base de datos para realizar operaciones CRUD sobre los proyectos.
    private ProjectRepository projectRepository;

    @Autowired //esta sirve permite inyectar la dependencia de CloudinaryService en ProjectService, lo que facilita la gestión de imágenes en la nube.
    private CloudinaryService cloudinaryService;

    public List<Project> getAllProjects() {
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Project getProjectById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
    }

    public Project createProject(ProjectRequest request, MultipartFile image) throws IOException {
        String imageUrl = cloudinaryService.uploadImage(image);

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setImageUrl(imageUrl);
        project.setLiveUrl(request.getLiveUrl());
        project.setGithubUrl(request.getGithubUrl());
        // NORMALIZACIÓN:
        // 1. Dividimos por la coma
        // 2. Quitamos espacios en blanco extra (trim)
        // 3. Filtramos los que estén vacíos
        if (request.getTechStack() != null) {
            List<String> techList = Arrays.stream(request.getTechStack().split(","))
                    .map(String::trim)
                    .filter(t -> !t.isEmpty())
                    .collect(Collectors.toList());
            project.setTechStack(techList);
        }
        // Convertimos el string a Enum. El .toUpperCase() es por seguridad
        try {
            project.setCategory(ProjectCategory.valueOf(request.getCategory().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoría no válida: " + request.getCategory());
        }

        return projectRepository.save(project);
    }

    public Project updateProject(UUID id, ProjectRequest request, MultipartFile image) throws IOException {
        Project existingProject = getProjectById(id);

        if (image != null && !image.isEmpty()) {
            String newImageUrl = cloudinaryService.uploadImage(image);
            existingProject.setImageUrl(newImageUrl);
        }

        existingProject.setTitle(request.getTitle());
        existingProject.setDescription(request.getDescription());
        // NORMALIZACIÓN:
        // 1. Dividimos por la coma
        // 2. Quitamos espacios en blanco extra (trim)
        // 3. Filtramos los que estén vacíos
        if (request.getTechStack() != null) {
            //array.stream hace que podamos trabajar con cada elemento del array de manera funcional
            //la otra forma de hacerlo sería con un for each, pero es más verboso
            //split funcionaba para separar el string en un array de strings, usando la coma como delimitador
            List<String> techList = Arrays.stream(request.getTechStack().split(","))
                    .map(String::trim)
                    .filter(t -> !t.isEmpty())
                    //el collect(Collectors.toList()) convierte el stream de strings en una lista de strings
                    //la otra forma de hacerlo sería con un for each
                    .collect(Collectors.toList());
            existingProject.setTechStack(techList);
        }
        existingProject.setLiveUrl(request.getLiveUrl());
        existingProject.setGithubUrl(request.getGithubUrl());
        // Convertimos el string a Enum. El .toUpperCase() es por seguridad
        try {
            existingProject.setCategory(ProjectCategory.valueOf(request.getCategory().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Categoría no válida: " + request.getCategory());
        }
        return projectRepository.save(existingProject);
    }

    @Transactional
    // Sirve para asegurar que la operación de borrado se realice dentro de una transacción, lo que garantiza la consistencia de los datos.
    public void deleteProject(UUID id) {
        // Es buena práctica verificar si existe antes de borrar
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("No existe el proyecto con ID: " + id);
        }
        projectRepository.deleteById(id);
    }
}
