package com.bryan.portafolioBackend.service;

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

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Project> getAllProjects() {
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Project getProjectById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con id: " + id));
    }

    public Project createProject(MultipartFile image, String title, String description) throws IOException {
        String imageUrl = cloudinaryService.uploadImage(image);

        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setImageUrl(imageUrl);

        return projectRepository.save(project);
    }

    public Project updateProject(UUID id, MultipartFile image, String title, String description) throws IOException {
        Project existingProject = getProjectById(id);

        if (image != null && !image.isEmpty()) {
            String newImageUrl = cloudinaryService.uploadImage(image);
            existingProject.setImageUrl(newImageUrl);
        }

        existingProject.setTitle(title);
        existingProject.setDescription(description);

        return projectRepository.save(existingProject);
    }

    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }
}
