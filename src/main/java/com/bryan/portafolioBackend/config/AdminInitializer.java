package com.bryan.portafolioBackend.config;

import com.bryan.portafolioBackend.model.AdminUser;
import com.bryan.portafolioBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Profile("!dev")
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminEmail.isEmpty() || adminPassword.isEmpty()) {
                System.out.println("[PROD] ADMIN_EMAIL o ADMIN_PASSWORD no configurados. Saltando creacion de admin.");
                return;
            }

            if (userRepository.findByEmail(adminEmail).isPresent()) {
                System.out.println("[PROD] Admin ya existe, no se crea uno nuevo.");
                return;
            }

            AdminUser admin = new AdminUser();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            userRepository.save(admin);
            System.out.println("[PROD] Usuario administrador creado con exito!");
        };
    }
}
