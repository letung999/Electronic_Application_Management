package com.ecommerce.electronicapplicationmanagement.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8082");
        server.setDescription("Electrical Store Development");

        Contact myContact = new Contact();
        myContact.setName("TungLe");
        myContact.setEmail("letung012000@gmail.com");

        Info information = new Info()
                .title("Electrical Store Management System API")
                .version("1.0")
                .description("This API exposes endpoints to manage electrical store.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
