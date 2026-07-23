package com.bryan.portafolioBackend.config;

import com.bryan.portafolioBackend.model.AdminUser;
import com.bryan.portafolioBackend.model.Project;
import com.bryan.portafolioBackend.model.ProjectCategory;
import com.bryan.portafolioBackend.repository.ProjectRepository;
import com.bryan.portafolioBackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DevDataSeeder {

    @Profile("dev")
    @Bean
    CommandLineRunner seedDevData(ProjectRepository projectRepository,
                                  UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            if (projectRepository.count() == 0) {
                Project proyectoPrueba = new Project();
                proyectoPrueba.setTitle("Mi Primer Proyecto Spring Boot");
                proyectoPrueba.setDescription("Migrando mi backend desde Next.js al ecosistema Java paso a paso.");
                proyectoPrueba.setImageUrl("https://ejemplo.com/mi-imagen.jpg");
                proyectoPrueba.setGithubUrl("https://github.com/tu-usuario");
                proyectoPrueba.setLiveUrl("https://tu-web.com");
                proyectoPrueba.setTechStack(List.of("Java", "Spring Boot", "PostgreSQL", "Docker"));
                proyectoPrueba.setCategory(ProjectCategory.BACKEND);
                projectRepository.save(proyectoPrueba);
                System.out.println("[DEV] Proyecto de prueba creado con exito!");
            }

            if (userRepository.count() == 0) {
                AdminUser admin = new AdminUser();
                admin.setEmail("bryan@admin.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                userRepository.save(admin);
                System.out.println("[DEV] Usuario administrador creado con exito!");
            }
        };
    }
}
