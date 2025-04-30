package com.israelopeters.ReadingNow_Backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setDescription("API Docs");

        Contact myContact = new Contact();
        myContact.setName("Israel Peters");
        myContact.setEmail("israelopeters@gmail.com");

        Info information = new Info()
                .title("Reading Now API")
                .version("1.0")
                .description("Backend API service to power the Reading Now Android app")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
