package com.bryan.portafolioBackend.model; // Ajusta tu paquete

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "admin_users")
@Data
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único autogenerado del usuario administrador")
    private UUID id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Correo electrónico del usuario administrador")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario administrador")
    private String password; // Aquí guardaremos la contraseña encriptada
}