package com.mmql.calculator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.email("quintana.m.martha@gmail.com");
        contact.setName("Martha Quintana");

        return new OpenAPI()
                .info(new Info().title("Calculadora API")
                        .description("API REST para operaciones aritméticas con historial y autenticación")
                        .version("1.0")
                        .contact(contact));
    }
}