package com.bryan.portafolioBackend.config; // Ajusta a tu paquete

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration // Le dice a Spring: "Oye, lee esto cuando arranques porque tiene configuraciones importantes"
public class CloudinaryConfig {

    @Bean // Le dice a Spring: "Crea este objeto Cloudinary y guárdalo para cuando lo necesitemos luego"
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();

        // OJO: Reemplaza estos textos por TUS credenciales reales de Cloudinary
        config.put("cloud_name", "ds8asomir");
        config.put("api_key", "888142678675681");
        config.put("api_secret", "irD6oPLjBYhIi2AtVZUuumFPG80");

        return new Cloudinary(config);
    }
}