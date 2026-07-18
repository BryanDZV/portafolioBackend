package com.bryan.portafolioBackend; // <-- Asegúrate de que coincida con tu paquete

import com.bryan.portafolioBackend.model.AdminUser;
import com.bryan.portafolioBackend.model.Project;
import com.bryan.portafolioBackend.model.ProjectCategory;
import com.bryan.portafolioBackend.repository.ProjectRepository;
import com.bryan.portafolioBackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class PortafolioBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortafolioBackendApplication.class, args);
	}

	// Este código se ejecuta automáticamente cada vez que le das al "Play"
	@Profile("dev") //  código solo se ejecute en tu PC local
	@Bean
	CommandLineRunner initData(ProjectRepository projectRepository,
	                           UserRepository userRepository,
	                           PasswordEncoder passwordEncoder) {
		return args -> {

			// --- Código de los proyectos SOLO DESARROLLO NO PRODUCCION ---
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
				System.out.println(" ¡Proyecto de prueba creado con éxito!");
			}

			//  Código del Usuario Administrador ---
			if (userRepository.count() == 0) {
				AdminUser admin = new AdminUser();
				admin.setEmail("bryan@admin.com");
				// Nunca guardamos la contraseña en texto plano, siempre encriptada
				admin.setPassword(passwordEncoder.encode("admin123"));

				userRepository.save(admin);
				System.out.println(" ¡Usuario administrador creado con éxito!");
			}
		};
	}
}