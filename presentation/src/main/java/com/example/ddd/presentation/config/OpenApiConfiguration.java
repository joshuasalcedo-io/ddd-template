package com.example.ddd.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("DDD Template API")
                .version("1.0.0")
                .description("REST API for Domain-Driven Design Template Application\n\n" +
                    "This API demonstrates DDD patterns including:\n" +
                    "- Aggregates and Entities\n" +
                    "- Value Objects\n" +
                    "- Domain Events\n" +
                    "- Repository Pattern\n" +
                    "- Use Case Pattern")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@example.com")
                    .url("https://example.com/support"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local development server"),
                new Server()
                    .url("https://api.example.com")
                    .description("Production server")
            ));
    }
}
