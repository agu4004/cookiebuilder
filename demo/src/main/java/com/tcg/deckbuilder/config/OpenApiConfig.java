package com.tcg.deckbuilder.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tcgDeckBuilderOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TCG Deck Builder API")
                        .description("REST API for managing trading card game cards and decks")
                        .version("1.0.0")
                        .contact(new Contact()
                            .name("TCG Deck Builder Team")
                            .email("support@tcgdeckbuilder.com")
                            .url("https://tcgdeckbuilder.com"))
                        .license(new License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                    new Server()
                        .url("http://localhost:8080")
                        .description("Development Server"),
                    new Server()
                        .url("https://api.tcgdeckbuilder.com")
                        .description("Production Server")
                ));
    }
}

