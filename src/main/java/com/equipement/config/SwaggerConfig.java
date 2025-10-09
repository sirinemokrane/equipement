package com.equipement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI equipementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion Équipements")
                        .description("Documentation de l'API pour la gestion des équipements (Câbles, Entretiens, etc.)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Votre Nom")
                                .email("votre.email@example.com")));
    }
}