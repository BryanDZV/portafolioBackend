package com.bryan.portafolioBackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration //sirve para que Spring Boot reconozca esta clase como una clase de configuración y la procese durante el arranque de la aplicación

public class SwaggerConfig {
    @Bean //un Bean es un objeto que Spring Boot crea y administra automáticamente, y que puede ser inyectado en otras partes de la aplicación cuando sea necesario. En este caso, estamos creando un Bean de tipo OpenAPI para configurar la documentación de la API.
    public OpenAPI customOpenAPI() {
        // 1. Creamos el molde del candado
        //usaremos un toke tipo bearer (JWT)
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        //2. Regla de seguridad para que todas las rutas de la API requieran autenticación
        SecurityRequirement securityRequeriment=new SecurityRequirement()
                .addList("bearerAuth");
        //server es para que swagger pueda funcionar en local y en render, ya que render no permite usar localhost
        Server server = new Server()
                .url("/")//así se adapta a cualquier entorno, ya que es relativa a la raíz del dominio
                .description("URL relativa (funciona en local y en Render)");

        //Objeto OpenAPI
        return new OpenAPI()
                .servers(List.of(server))//aqui agregamos el server a la configuracion de swagger
                .info(new Info()
                //A mi información
                .title("API Portafolio - Bryan")
                .version("1.0.0")
                .description("Documentación oficial de la API REST para el portafolio personal de Bryan. Incluye autenticación JWT y gestión de proyectos.")
                .contact(new Contact()
                        .name("Bryan Zavala")
                        .url("https://portafolio-alpha-rosy-19.vercel.app/es")
                        .email("dev.bryanzavala@gmail.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT")))
        //B inyectar la seguridad a los componentes
        .components(new Components()
                .addSecuritySchemes("bearerAuth", securityScheme))
        //3. Agregamos la regla de seguridad a la API  en cada endpoint
        .addSecurityItem(securityRequeriment);
    }
}
