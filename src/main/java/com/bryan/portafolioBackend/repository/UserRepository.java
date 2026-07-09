package com.bryan.portafolioBackend.repository; // Ajusta tu paquete

import com.bryan.portafolioBackend.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AdminUser, UUID> {
    // Este método mágico le dice a Spring: "Búscame un usuario que tenga este email exacto"
    Optional<AdminUser> findByEmail(String email);
}