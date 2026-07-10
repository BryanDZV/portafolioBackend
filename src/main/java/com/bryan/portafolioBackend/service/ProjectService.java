package com.bryan.portafolioBackend.service;

import com.bryan.portafolioBackend.dto.ProjectRequest;
import com.bryan.portafolioBackend.model.Project;
import com.bryan.portafolioBackend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
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

        return projectRepository.save(existingProject);
    }

    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }
}
